package com.vomiter.mobsuseshields.mixin;

import com.vomiter.mobsuseshields.Config;
import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import com.vomiter.mobsuseshields.common.ShieldAnticipation;
import com.vomiter.mobsuseshields.common.entity.ai.MobShieldCombatStatus;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements ICanUseShieldMob{
    @Unique
    boolean MUS$SHIELD_DISABLED = false;
    @Unique
    boolean MUS$CAN_USE_SHIELD = false;
    @Unique
    boolean MUS$SHIELD_GOAL_INJECTED = false;
    @Unique
    long MUS$NEXT_SHIELD_ALLOWED_TICK = 0;
    @Unique
    MobShieldCombatStatus MUS$STATUS = MobShieldCombatStatus.ATTACK;
    @Unique
    ShieldAnticipation MUS$ANTICIPATION;

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
    public ShieldAnticipation mus$getAnticipation() {
        if(MUS$ANTICIPATION == null){
            MUS$ANTICIPATION = new ShieldAnticipation((Mob)(Object)this);
        }
        return MUS$ANTICIPATION;
    }

    @Override
    public MobShieldCombatStatus mus$getMobShieldCombatStatus(){
        return MUS$STATUS;
    }

    @Override
    public void mus$setMobShieldCombatStatus(MobShieldCombatStatus status){
        MUS$STATUS = status;
    }


    @Override
    public boolean mus$shouldAnticipate() {
        return mus$getAnticipation().defaultShouldAnticipate() || Config.MOB_CONSUME_SHIELD_DURABILITY;
    }

    @Override
    public void mus$diableShield(boolean b){
        MUS$SHIELD_DISABLED = b;
    }

    @Override
    public boolean mus$isShieldDisabled(){
        return MUS$SHIELD_DISABLED;
    }
}
