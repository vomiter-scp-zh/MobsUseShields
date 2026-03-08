package com.vomiter.mobsuseshields.mixin.debug;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntity_ShieldBroadcastDebugMixin {

    @Shadow
    protected float lastHurt;
    private static final Logger LOG = LoggerFactory.getLogger("MobShieldDebug");

    @Redirect(
        method = "hurt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"
        )
    )
    private void mus$debugBroadcastEntityEvent(Level level, Entity entity, byte eventId) {
        LivingEntity self = (LivingEntity) (Object) this;

        LOG.info(
            "[BroadcastEntityEvent] entity={} eventId={} invul={} lastHurt={}",
            self.getType().toShortString(),
            eventId,
            self.invulnerableTime,
            lastHurt
        );

        level.broadcastEntityEvent(entity, eventId);
    }
}