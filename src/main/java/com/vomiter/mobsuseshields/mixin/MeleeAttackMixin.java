package com.vomiter.mobsuseshields.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MeleeAttack.class)
public class MeleeAttackMixin {

    @WrapOperation(
            method = "*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;isWithinMeleeAttackRange(Lnet/minecraft/world/entity/LivingEntity;)Z"
            ),
            require = 1
    )
    private static boolean mus$checkHoldingShield(Mob instance, LivingEntity target, Operation<Boolean> original) {
        if (instance.isBlocking()) {
            return false;
        }
        return original.call(instance, target);
    }
}
