package com.vomiter.mobsuseshields.mixin.client;

import com.vomiter.mobsuseshields.client.ClientEventHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {

    @Inject(
        method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
        at = @At("HEAD")
    )
    private void mus$applyBlockUsePose(
            HumanoidRenderState renderState, CallbackInfo ci
    ) {
        boolean offhandShield = renderState.getRenderDataOrDefault(
                ClientEventHandler.OFFHAND_SHIELD,
                false
        );

        boolean usingShield = renderState.getRenderDataOrDefault(
                ClientEventHandler.USING_SHIELD,
                false
        );


    }
}