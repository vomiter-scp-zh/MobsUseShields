package com.vomiter.mobsuseshields.mixin;

import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements ICanUseShieldMob{
    @Unique
    boolean MUS$CAN_USE_SHIELD = false;
    @Unique
    boolean MUS$SHIELD_GOAL_INJECTED = false;
    @Unique
    long MUS$NEXT_SHIELD_ALLOWED_TICK = 0;
    @Unique
    long MUS$LAST_SHIELD_ATTEMPT_TICK = 0;

    protected MobMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    public void mus$setCanUseShield(boolean b) {
        MUS$CAN_USE_SHIELD = b;
    }

    @Override
    public boolean mus$canUseShield() {
        return MUS$CAN_USE_SHIELD;
    }

    @Override
    public boolean mus$shieldGoalsInjected() {
        return MUS$SHIELD_GOAL_INJECTED;
    }

    @Override
    public void mus$setShieldGoalsInjected(boolean value) {
        MUS$SHIELD_GOAL_INJECTED = value;
    }

    @Override
    public long mus$getNextShieldAllowedTick() {
        return MUS$NEXT_SHIELD_ALLOWED_TICK;
    }

    @Override
    public void mus$setNextShieldAllowedTick(long tick) {
        MUS$NEXT_SHIELD_ALLOWED_TICK = tick;
    }

    @Override
    public long mus$getLastAttemptToUseShield() {
        return MUS$LAST_SHIELD_ATTEMPT_TICK;
    }

    @Override
    public void mus$setLastAttemptToUseShield() {
        MUS$LAST_SHIELD_ATTEMPT_TICK = level().getGameTime();
    }
}
