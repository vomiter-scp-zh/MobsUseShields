package com.vomiter.mobsuseshields.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntity_ShieldBlockEffectsMixin {
    @WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playHurtSound(Lnet/minecraft/world/damagesource/DamageSource;)V"))
    private void noPlayHurtSound(
            LivingEntity instance,
            DamageSource p_21160_,
            Operation<Void> original,
            @Local(name = "flag") boolean completeBlock
    ){
        if(completeBlock) {
            instance.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + instance.level().random.nextFloat() * 0.4F);
            return;
        }
        original.call(instance, p_21160_);
    }

    @WrapOperation(
            method = "hurt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"
            )
    )
    private void noKnockBack(
            LivingEntity instance,
            double strength,
            double x,
            double z,
            Operation<Void> original,
            @Local(name = "flag") boolean completeBlock
    ){
        if(completeBlock) {
            original.call(instance, strength * 0.2, x, z);
            return;
        }
        original.call(instance, strength, x, z);
    }

    @Inject(
            method = "blockUsingShield",
            at = @At("HEAD"),
            cancellable = true
    )
    private void noKnockBack2(
            LivingEntity attacker, CallbackInfo ci
    ){
        if(this instanceof ICanUseShieldMob){
            if(attacker instanceof HoglinBase || attacker instanceof Ravager) return;
            ci.cancel();
        }
    }


}