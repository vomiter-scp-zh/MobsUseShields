package com.vomiter.mobsuseshields.mixin.debug;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntity_ClientShieldSoundDebugMixin {

    @Unique
    private static final Logger MUS_LOG = LoggerFactory.getLogger("MobShieldDebug");

    @Redirect(
        method = "handleEntityEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
        )
    )
    private void mus$debugPlaySoundInHandleEntityEvent(
        LivingEntity self,
        SoundEvent sound,
        float volume,
        float pitch,
        byte eventId
    ) {
        Level level = self.level();

        if (level.isClientSide && eventId == 29) {
            MUS_LOG.info(
                "[ClientShieldSound] entity={} eventId={} sound={} volume={} pitch={}",
                self.getType().toShortString(),
                eventId,
                sound == SoundEvents.SHIELD_BLOCK,
                volume,
                pitch
            );
        }

        self.playSound(sound, volume, pitch);
    }
}