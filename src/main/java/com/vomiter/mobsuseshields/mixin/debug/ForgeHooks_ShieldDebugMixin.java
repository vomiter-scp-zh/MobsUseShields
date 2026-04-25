package com.vomiter.mobsuseshields.mixin.debug;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//@Mixin(value = ForgeHooks.class, remap = false)
public abstract class ForgeHooks_ShieldDebugMixin {
    /*

    private static final Logger LOG = LoggerFactory.getLogger("MobShieldDebug");

    @Inject(
        method = "onShieldBlock",
        at = @At("RETURN")
    )
    private static void mus$debugShieldEvent(
        LivingEntity entity,
        DamageSource source,
        float amount,
        CallbackInfoReturnable<ShieldBlockEvent> cir
    ) {
        ShieldBlockEvent ev = cir.getReturnValue();

        LOG.info(
            "[ShieldEvent] entity={} incoming={} blocked={} shieldDamage={} canceled={}",
            entity.getType().toShortString(),
            amount,
            ev.getBlockedDamage(),
            ev.shieldTakesDamage(),
            ev.isCanceled()
        );
    }

     */
}