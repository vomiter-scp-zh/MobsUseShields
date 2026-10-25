package com.vomiter.mobsuseshields.mixin.compat.iwa;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.mobsuseshields.ClientConfig;
import com.vomiter.mobsuseshields.client.ClientEventHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tallestegg.illagersweararmor.client.model.IllagerArmorModel;

/** IWA 3.x keeps vanilla illagers and animates a separate armor model. */
@Mixin(value = IllagerArmorModel.class, remap = false)
public abstract class IllagerArmorModelMixin extends HumanoidModel<IllagerRenderState> {
    protected IllagerArmorModelMixin(ModelPart root) {
        super(root);
    }

    @WrapOperation(
            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/IllagerRenderState;)V",
            at = @At(value = "INVOKE", target = "Ltallestegg/illagersweararmor/client/render/render_layers/NonHumanoidArmorLayer;armorCrossArms(Lnet/minecraft/client/model/HumanoidModel;)V")
    )
    private void mus$keepArmorArmsOpen(HumanoidModel<?> model, Operation<Void> original,
                                     @Local(argsOnly = true) IllagerRenderState state) {
        boolean visibleShield = !ClientConfig.HIDE_PILLAGER_SHIELD_IN_ARMS
                && state.getRenderDataOrDefault(ClientEventHandler.OFFHAND_SHIELD, false);
        if (!visibleShield && !state.getRenderDataOrDefault(ClientEventHandler.USING_SHIELD, false)) {
            original.call(model);
        }
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/IllagerRenderState;)V", at = @At("TAIL"))
    private void mus$matchBlockingArms(IllagerRenderState state, CallbackInfo ci) {
        if (!state.getRenderDataOrDefault(ClientEventHandler.USING_SHIELD, false)) return;
        // Match the vanilla illager pose applied by IllagerModelMixin.
        boolean right = (state.useItemHand == InteractionHand.MAIN_HAND) == (state.mainArm == HumanoidArm.RIGHT);
        ModelPart blockingArm = right ? rightArm : leftArm;
        ModelPart otherArm = right ? leftArm : rightArm;
        rightArm.x = -5.0F;
        leftArm.x = 5.0F;
        rightArm.y = leftArm.y = 2.0F;
        rightArm.z = leftArm.z = 0.0F;
        blockingArm.xRot = -0.2F;
        blockingArm.yRot = right ? -0.52F : 0.52F;
        blockingArm.zRot = 0.0F;
        otherArm.xRot = -1.0F;
        otherArm.yRot = otherArm.zRot = 0.0F;
    }
}
