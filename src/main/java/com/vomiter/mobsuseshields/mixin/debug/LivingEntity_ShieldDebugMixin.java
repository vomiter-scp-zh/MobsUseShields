package com.vomiter.mobsuseshields.mixin.debug;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntity_ShieldDebugMixin {

    @Unique
    private static final Logger LOG = LoggerFactory.getLogger("MobShieldDebug");

    @Inject(
        method = "hurt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/common/ForgeHooks;onShieldBlock(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)Lnet/minecraftforge/event/entity/living/ShieldBlockEvent;",
            shift = At.Shift.AFTER
        )
    )
    private void mus$debugShieldBlock(
        DamageSource source,
        float amount,
        CallbackInfoReturnable<Boolean> cir
    ) {
        LivingEntity self = (LivingEntity)(Object)this;

        boolean blocking = self.isBlocking();
        boolean directionBlocked = self.isDamageSourceBlocked(source);

        LOG.info(
            "[ShieldDebug] entity={} source={} incomingDamage={} blocking={} directionBlocked={}",
            self.getType().toShortString(),
            source.getMsgId(),
            amount,
            blocking,
            directionBlocked
        );
    }
}