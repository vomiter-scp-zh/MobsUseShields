package com.vomiter.mobsuseshields.common.entity.ai;

import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import com.vomiter.neurolib.common.entity.ai.MutatedMeleeGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class MobUseShieldAttackGoal extends MutatedMeleeGoal {
    MeleeAttackGoal basic;
    private long nextCheckContinueToUseAtTick;
    private final int checkContinueToUseInterval = 30;

    public MobUseShieldAttackGoal(MeleeAttackGoal basicGoal){
        super(basicGoal);
        basic = basicGoal;
        this
            .setExtraUseCheck(goal -> !(mob instanceof ICanUseShieldMob shieldMob) || (shieldMob.mus$getMobShieldCombatStatus() != MobShieldCombatStatus.SHIELDING))
            .setExtraContinueCheck(goal -> {
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
                            return true;
                        }
                );
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
