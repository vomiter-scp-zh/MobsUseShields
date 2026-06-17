package com.vomiter.mobsuseshields.mixin;

import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void attemptToShield(ServerLevel p_376221_, DamageSource p_376460_, float p_376610_, CallbackInfoReturnable<Boolean> cir){
        if((Object)this instanceof ICanUseShieldMob shieldMob){
            shieldMob.mus$attemptToShield();
        }
    }
}
