package com.vomiter.mobsuseshields.mixin.debug;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntity_ClientEntityEventDebugMixin {

    @Unique
    private static final Logger MUS_LOG = LoggerFactory.getLogger("MobShieldDebug");

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void mus$debugHandleEntityEvent(byte eventId, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        Level level = self.level();

        if (level.isClientSide && eventId == 29) {
            MUS_LOG.info(
                "[ClientHandleEntityEvent] entity={} eventId={} clientSide={}",
                self.getType().toShortString(),
                eventId,
                level.isClientSide
            );
        }
    }
}