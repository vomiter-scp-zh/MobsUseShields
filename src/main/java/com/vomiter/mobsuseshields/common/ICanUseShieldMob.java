package com.vomiter.mobsuseshields.common;

import com.vomiter.mobsuseshields.common.entity.ai.MobShieldCombatStatus;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ShieldItem;

public interface ICanUseShieldMob {
    void mus$setCanUseShield(boolean b);
    boolean mus$canUseShield();
    boolean mus$shieldGoalsInjected();
    void mus$setShieldGoalsInjected(boolean value);
    long mus$getNextShieldAllowedTick();
    void mus$setNextShieldAllowedTick(long tick);
    ShieldAnticipation mus$getAnticipation();
    MobShieldCombatStatus mus$getMobShieldCombatStatus();
    void mus$setMobShieldCombatStatus(MobShieldCombatStatus status);
    void mus$diableShield(boolean b);
    boolean mus$isShieldDisabled();

    default void mus$attemptToShield(){
        if(this instanceof Mob mob && !mob.level().isClientSide()){
            var currentTime = mob.level().getGameTime();
            if(currentTime >= mus$getNextShieldAllowedTick()){
                if(shouldStartShielding(mob)) mus$setMobShieldCombatStatus(MobShieldCombatStatus.SHIELDING);
            }
        }
    }

    boolean mus$shouldAnticipate();

    static boolean shouldStartShielding(Mob mob) {
        var shieldMob = (ICanUseShieldMob) mob;
        if (!shieldMob.mus$canUseShield()) return false;
        if (!(mob.getOffhandItem().getItem() instanceof ShieldItem)) return false;

        LivingEntity target = mob.getTarget();
        long currentTime = mob.level().getGameTime();
        if(currentTime < shieldMob.mus$getNextShieldAllowedTick()) return false;
        if (mob.getHealth() >= mob.getMaxHealth()) return false;
        if (mob.isUsingItem()) return false;
        if (target != null && shieldMob.mus$shouldAnticipate()){
            return ShieldAnticipation.anticipate(target, mob);
        }
        return true;
    }

    static boolean shouldKeepShielding(Mob mob) {
        var shieldMob = (ICanUseShieldMob) mob;
        if (!shieldMob.mus$canUseShield()) return false;
        if (!(mob.getOffhandItem().getItem() instanceof ShieldItem)) return false;
        if (shieldMob.mus$isShieldDisabled()) return false;

        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (shieldMob.mus$shouldAnticipate()){
            return ShieldAnticipation.anticipate(target, mob);
        }
        return true;
    }
}
