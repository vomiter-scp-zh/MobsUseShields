package com.vomiter.mobsuseshields.mixin.client;

import com.vomiter.mobsuseshields.MobsUseShields;
import com.vomiter.mobsuseshields.client.ClientEventHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.zombie.AbstractZombieModel;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractZombieModel.class)
public abstract class AbstractZombieModelMixin<T extends ZombieRenderState> extends HumanoidModel<@NotNull T> {
    private AbstractZombieModelMixin(ModelPart p_170677_) {
        super(p_170677_);
    }

    @Inject(
        method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;)V",
        at = @At("TAIL")
    )
    private void mus$reapplyShieldBlockPose(
            ZombieRenderState renderState, CallbackInfo ci
    ) {
        if (!renderState.isUsingItem) return;
        boolean usingShield = renderState.getRenderDataOrDefault(
                ClientEventHandler.USING_SHIELD,
                false
        );
        if(!usingShield) return;

        if (renderState.useItemHand == InteractionHand.MAIN_HAND) {
            this.rightArm.xRot = -0.95F;
            this.rightArm.yRot = -0.52F;
            this.rightArm.zRot = 0.0F;
        } else {
            this.leftArm.xRot = -0.95F;
            this.leftArm.yRot = 0.52F;
            this.leftArm.zRot = 0.0F;
        }

    }

}