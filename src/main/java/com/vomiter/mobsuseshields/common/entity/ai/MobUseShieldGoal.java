package com.vomiter.mobsuseshields.common.entity.ai;

import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ShieldItem;

import java.util.EnumSet;

public class MobUseShieldGoal extends Goal {
    private final Mob mob;
    private final ICanUseShieldMob shieldMob;

    private final int useDuration;
    private final int cooldownDuration;

    private long stopShieldAtTick;
    private long stopShieldAtTickIfNoTarget;
    private boolean shieldExhaustion;

    public MobUseShieldGoal(Mob mob, ICanUseShieldMob shieldMob) {
        this(mob, shieldMob, 60, 120);
    }

    public MobUseShieldGoal(Mob mob, ICanUseShieldMob shieldMob, int useDuration, int cooldownDuration) {
        this.mob = mob;
        this.shieldMob = shieldMob;
        this.useDuration = useDuration;
        this.cooldownDuration = cooldownDuration;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return ICanUseShieldMob.shouldStartShielding(mob);
    }

    @Override
    public boolean canContinueToUse() {
        if (!shieldMob.mus$canUseShield()) return false;
        if (!(mob.getOffhandItem().getItem() instanceof ShieldItem)) return false;

        if(currentTime() > stopShieldAtTick){
            shieldExhaustion = true;
            return false;
        }

        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) {
            if(currentTime() > stopShieldAtTickIfNoTarget) return false;
        }

        return true;
    }

    @Override
    public void start() {
        stopShieldAtTick = mob.level().getGameTime() + useDuration;
        updateTickWhenTargetPresent();
        mob.getNavigation().stop();
        mob.startUsingItem(InteractionHand.OFF_HAND);
    }

    @Override
    public void stop() {
        mob.stopUsingItem();
        if(shieldExhaustion) shieldMob.mus$setNextShieldAllowedTick(currentTime() + cooldownDuration);
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        //mob.getNavigation().stop();

        if (target != null) {
            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            updateTickWhenTargetPresent();
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private void updateTickWhenTargetPresent(){
        stopShieldAtTickIfNoTarget = currentTime() + 10;
    }

    private long currentTime(){
        return mob.level().getGameTime();
    }
}