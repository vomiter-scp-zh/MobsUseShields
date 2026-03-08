package com.vomiter.mobsuseshields.common;

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
    long mus$getLastAttemptToUseShield();
    void mus$setLastAttemptToUseShield();

    static boolean shouldStartShielding(Mob mob) {
        var shieldMob = (ICanUseShieldMob) mob;
        if (!shieldMob.mus$canUseShield()) return false;
        if (!(mob.getOffhandItem().getItem() instanceof ShieldItem)) return false;

        LivingEntity target = mob.getTarget();
        long currentTime = mob.level().getGameTime();
        if(currentTime < shieldMob.mus$getNextShieldAllowedTick()) return false;
        if (mob.getHealth() >= mob.getMaxHealth()) return false; // debug條件

        if (mob.isUsingItem()) return false;
        return currentTime < shieldMob.mus$getLastAttemptToUseShield() + 10;
    }

}
