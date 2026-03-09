package com.vomiter.mobsuseshields.common;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TridentItem;

public class ShieldAnticipation {
    private static final int THRESHOLD_UPDATE_INTERVAL = 1000;
    private static final int UPDATE_INTERVAL = 20;
    private final Mob mob;


    long NEXT_UPDATE_THRESHOLD_TICK = -1;
    long NEXT_UPDATE_TICK = -1;
    static float ANTICIPATION_THRESHOLD = 0;
    boolean CACHED_VALUE = false;

    public ShieldAnticipation(Mob mob) {
        this.mob = mob;
    }

    public boolean defaultShouldAnticipate(){
        if(mob.level() instanceof ServerLevel serverLevel){
            long currentTime = serverLevel.getGameTime();
            if(NEXT_UPDATE_TICK < currentTime || NEXT_UPDATE_TICK > currentTime + 5L * UPDATE_INTERVAL){
                NEXT_UPDATE_TICK = currentTime + UPDATE_INTERVAL;
            }
            else return CACHED_VALUE;

            if(NEXT_UPDATE_THRESHOLD_TICK < currentTime || NEXT_UPDATE_THRESHOLD_TICK > currentTime + 5L * THRESHOLD_UPDATE_INTERVAL){
                ANTICIPATION_THRESHOLD = serverLevel.getRandom().nextFloat();
                NEXT_UPDATE_THRESHOLD_TICK = currentTime + THRESHOLD_UPDATE_INTERVAL;
            }
            DifficultyInstance di = serverLevel.getCurrentDifficultyAt(mob.blockPosition());
            //  0.0 ~ 6.75（wiki 的 Local Difficulty）
            float local = di.getEffectiveDifficulty();
            float local01 = Mth.clamp(local / 6.75f, 0.0f, 1.0f);
            float totalHpLoss = 1 - (mob.getHealth() / mob.getMaxHealth());
            CACHED_VALUE = local01 * totalHpLoss >= ANTICIPATION_THRESHOLD;
        }
        return CACHED_VALUE;
    }

    static boolean anticipate(LivingEntity target, Mob mob){
        var targetUsingStack = target.getUseItem();
        var targetUsing = targetUsingStack.getItem();
        if (targetUsing instanceof BowItem) return true;
        if (targetUsing instanceof TridentItem) return true;
        if (targetUsing instanceof ShieldItem) return false;
        if (targetUsing.isEdible()) return false;
        if(target.getMainHandItem().getItem() instanceof CrossbowItem && CrossbowItem.isCharged(target.getMainHandItem())) return true;
        if(target.getOffhandItem().getItem() instanceof CrossbowItem && CrossbowItem.isCharged(target.getOffhandItem())) return true;
        var attackAttributeInstance = target.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttributeInstance == null) return false;
        if (attackAttributeInstance.getValue() >= mob.getHealth() * 0.5 && mob.distanceToSqr(target) < 4 * 4) return true;
        return false;
    }

}
