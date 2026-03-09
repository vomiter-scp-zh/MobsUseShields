package com.vomiter.mobsuseshields.common.entity.ai;

import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import com.vomiter.mobsuseshields.mixin.MeleeAttackGoalAccessor;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class MobUseShieldAttackGoal extends MeleeAttackGoal {
    MeleeAttackGoal basic;
    private long nextCheckContinueToUseAtTick;
    private final int checkContinueToUseInterval = 30;

    public MobUseShieldAttackGoal(MeleeAttackGoalAccessor basicGoal){
        super(basicGoal.getMob(), basicGoal.getSpeedModifier(), basicGoal.getFollowOption());
        basic = (MeleeAttackGoal) basicGoal;
    }

    @Override
    public boolean canUse() {
        if (mob instanceof ICanUseShieldMob shieldMob && (shieldMob.mus$getMobShieldCombatStatus() == MobShieldCombatStatus.SHIELDING)) {
            return false;
        }
        return basic.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (mob instanceof ICanUseShieldMob shieldMob){
            if(shieldMob.mus$getMobShieldCombatStatus() == MobShieldCombatStatus.SHIELDING) {
                return false;
            }
            var currentTime = mob.level().getGameTime();
            if(shieldMob.mus$shouldAnticipate() && currentTime >= nextCheckContinueToUseAtTick){
                nextCheckContinueToUseAtTick = currentTime + checkContinueToUseInterval;
                shieldMob.mus$attemptToShield();
            }
        }
        return basic.canContinueToUse();
    }

    @Override
    public void start(){
        basic.start();
    }

    @Override
    public void stop() {
        basic.stop();
    }

    @Override
    public void tick() {
        basic.tick();
    }
}
