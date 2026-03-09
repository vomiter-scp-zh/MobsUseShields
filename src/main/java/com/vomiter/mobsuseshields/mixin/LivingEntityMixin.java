package com.vomiter.mobsuseshields.mixin;

import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
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

    @Inject(method = "hurt", at = @At("RETURN"))
    private void attemptToShield(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> cir){
        if((Object)this instanceof ICanUseShieldMob shieldMob){
            shieldMob.mus$attemptToShield();
        }
    }
}
