package com.vomiter.mobsuseshields.common.entity.ai;

import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import com.vomiter.neurolib.common.entity.generic.EntityControlHelpers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.item.ShieldItem;

import java.util.EnumSet;

public class MobUseShieldGoal extends Goal {
    private final Mob mob;
    private final ICanUseShieldMob shieldMob;

    private final int useDuration;
    private final int cooldownDuration;
    private final int checkContinueToUseInterval;

    private long stopShieldAtTick;
    private long stopShieldAtTickIfNoTarget;
    private boolean shieldExhaustion;
    private long nextCheckContinueToUseAtTick;

    public MobUseShieldGoal(Mob mob, ICanUseShieldMob shieldMob, int useDuration, int cooldownDuration, int checkContinueToUseInterval) {
        this.mob = mob;
        this.shieldMob = shieldMob;
        this.useDuration = useDuration;
        this.cooldownDuration = cooldownDuration;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.checkContinueToUseInterval = checkContinueToUseInterval;
    }

    @Override
    public boolean canUse() {
        var status = shieldMob.mus$getMobShieldCombatStatus();
        return status == MobShieldCombatStatus.SHIELDING;
    }

    @Override
    public boolean canContinueToUse() {
        if (!shieldMob.mus$canUseShield()) return false;
        if (!(mob.getOffhandItem().getItem() instanceof ShieldItem)) return false;
        if (shieldMob.mus$isShieldDisabled()) return false;

        if(currentTime() >= stopShieldAtTick){
            shieldExhaustion = true;
            return false;
        }

        if(currentTime() >= nextCheckContinueToUseAtTick){
            nextCheckContinueToUseAtTick = currentTime() + checkContinueToUseInterval;
            return ICanUseShieldMob.shouldKeepShielding(mob);
        }

        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) {
            if(currentTime() >= stopShieldAtTickIfNoTarget) return false;
        }
        return true;
    }

    @Override
    public void start() {
        var currentTime = mob.level().getGameTime();
        stopShieldAtTick = currentTime + useDuration;
        nextCheckContinueToUseAtTick = currentTime + checkContinueToUseInterval;
        updateTickWhenTargetPresent();
        mob.getNavigation().stop();
        mob.startUsingItem(InteractionHand.OFF_HAND);
        shieldExhaustion = false;
        shieldMob.mus$setMobShieldCombatStatus(MobShieldCombatStatus.SHIELDING);
        shieldMob.mus$diableShield(false);
    }

    @Override
    public void stop() {
        mob.stopUsingItem();
        if(shieldExhaustion) {
            shieldMob.mus$setNextShieldAllowedTick(currentTime() + cooldownDuration);
        }
        shieldMob.mus$setMobShieldCombatStatus(MobShieldCombatStatus.ATTACK);
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        //mob.getNavigation().stop();

        if (target != null) {
            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            updateTickWhenTargetPresent();
            double dx = target.getX() - mob.getX();
            double dz = target.getZ() - mob.getZ();
            float targetYaw = (float) (net.minecraft.util.Mth.atan2(dz, dx) * (180F / Math.PI)) - 90.0F;
            mob.setYRot(targetYaw);
            if(mob.tickCount % 3 == 0) EntityControlHelpers.nudgeTowardTarget(mob, target);
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