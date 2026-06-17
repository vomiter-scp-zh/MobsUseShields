package com.vomiter.mobsuseshields.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.mobsuseshields.Config;
import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntity_ShieldBlockEffectsMixin extends Entity {
    LivingEntity_ShieldBlockEffectsMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @WrapOperation(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playHurtSound(Lnet/minecraft/world/damagesource/DamageSource;)V"))
    private void mus$noPlayHurtSound(
            LivingEntity instance,
            DamageSource p_21160_,
            Operation<Void> original,
            @Local(name = "flag") boolean completeBlock
    ){
        if(completeBlock) {
            instance.playSound(SoundEvents.SHIELD_BLOCK.value(), 1.0F, 0.8F + instance.level().random.nextFloat() * 0.4F);
            return;
        }
        original.call(instance, p_21160_);
    }

    @WrapOperation(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"
            )
    )
    private void mus$noKnockBack(
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
            method = "blockUsingItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mus$shieldInteract(
            ServerLevel level, LivingEntity attacker, CallbackInfo ci
    ){
        if(this instanceof ICanUseShieldMob shieldMob && (Object)this instanceof Mob mob){

            Weapon weapon = attacker.getMainHandItem().get(DataComponents.WEAPON);
            boolean canDisableBlocking = weapon != null && weapon.disableBlockingForSeconds() > 0;

            if(attacker.getSecondsToDisableBlocking() > 0 || canDisableBlocking){
                shieldMob.mus$setNextShieldAllowedTick(level().getGameTime() + 20 * 5);
                shieldMob.mus$diableShield(true);
            }

            if(attacker instanceof HoglinBase || attacker instanceof Ravager) return;
            ci.cancel();
        }
    }

}