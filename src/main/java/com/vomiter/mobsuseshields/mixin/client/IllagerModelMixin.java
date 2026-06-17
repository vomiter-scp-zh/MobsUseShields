package com.vomiter.mobsuseshields.mixin.client;

import com.vomiter.mobsuseshields.ClientConfig;
import com.vomiter.mobsuseshields.MobsUseShields;
import com.vomiter.mobsuseshields.client.ClientEventHandler;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllagerModel.class)
public class IllagerModelMixin {
    @Shadow
    @Final
    private ModelPart rightArm;

    @Shadow
    @Final
    private ModelPart leftArm;

    @Shadow
    @Final
    private ModelPart arms;

    @Inject(
            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/IllagerRenderState;)V",
            at = @At("TAIL")
    )
    private void mus$reapplyShieldBlockPose(
            IllagerRenderState renderState, CallbackInfo ci
    ) {
        boolean offhandShield = renderState.getRenderDataOrDefault(
                ClientEventHandler.OFFHAND_SHIELD,
                false
        );

        boolean usingShield = renderState.getRenderDataOrDefault(
                ClientEventHandler.USING_SHIELD,
                false
        );

        if (!ClientConfig.HIDE_PILLAGER_SHIELD_IN_ARMS) {
            if (offhandShield) {
                this.arms.visible = false;
                this.rightArm.visible = true;
                this.leftArm.visible = true;
            }
        }

        if (!usingShield) return;

        this.arms.visible = false;
        this.rightArm.visible = true;
        this.leftArm.visible = true;

        // 先清掉殘留 POSE
        this.rightArm.x = -5.0F;
        this.rightArm.y = 2.0F;
        this.rightArm.z = 0.0F;
        this.rightArm.xRot = 0.0F;
        this.rightArm.yRot = 0.0F;
        this.rightArm.zRot = 0.0F;

        this.leftArm.x = 5.0F;
        this.leftArm.y = 2.0F;
        this.leftArm.z = 0.0F;
        this.leftArm.xRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        this.leftArm.zRot = 0.0F;

        if (renderState.useItemHand == InteractionHand.MAIN_HAND) {
            //wierd, but it seems to consider it's using main hand despite the shield being in offhand
            //or is vindicator's left hand its main hand?
            this.rightArm.xRot = -0.2F;
            this.rightArm.yRot = -0.52F;
            this.rightArm.zRot = 0.0F;

            this.leftArm.xRot = -1F;
            this.leftArm.yRot = 0.0F;
            this.leftArm.zRot = 0.0F;
        } else {
            this.leftArm.xRot = -0.2F;
            this.leftArm.yRot = 0.52F;
            this.leftArm.zRot = 0.0F;

            this.rightArm.xRot = -1F;
            this.rightArm.yRot = 0.0F;
            this.rightArm.zRot = 0.0F;
        }
    }
}
