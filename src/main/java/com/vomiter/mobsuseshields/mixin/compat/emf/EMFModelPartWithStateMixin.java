package com.vomiter.mobsuseshields.mixin.compat.emf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_model_features.models.animation.state.EMFState;
import traben.entity_model_features.models.parts.EMFModelPart;
import traben.entity_model_features.models.parts.EMFModelPartRoot;
import traben.entity_model_features.models.parts.EMFModelPartVanilla;
import traben.entity_model_features.models.parts.EMFModelPartWithState;

import java.util.List;
import java.util.Map;

@Mixin(value = EMFModelPartWithState.class)
public abstract class EMFModelPartWithStateMixin extends EMFModelPart{

    public EMFModelPartWithStateMixin(List<Cube> cuboids, Map<String, ModelPart> children, EMFModelPartRoot root) {
        super(cuboids, children, root);
    }

    @Shadow
    abstract EMFModelPartWithState.EMFModelState getCurrentState();

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Ltraben/entity_model_features/models/parts/EMFModelPartRoot;animate()V",
                    shift = At.Shift.AFTER
            ),
            require = 0
    )
    private void mus$applyBlockingPoseAfterEmfAnimation_322(
            PoseStack matrices, VertexConsumer vertices, int light, int overlay, int k, CallbackInfo ci
    ) {
        if (!(((Object) this) instanceof EMFModelPartVanilla vanillaPart)) {
            return;
        }

        var emfEntity = EMFState.state().emfEntity();

        if (!(emfEntity instanceof LivingEntity entity)) {
            return;
        }

        if (!(entity instanceof Mob)) {
            return;
        }

        if (!entity.isUsingItem()) {
            return;
        }

        ItemStack using = entity.getUseItem();
        if (using.isEmpty() || using.getUseAnimation() != UseAnim.BLOCK) {
            return;
        }

        String partName = ((EMFModelPartVanillaAccessor) vanillaPart).mus$getName();
        boolean useRight = mus$usingRightArm(entity);

        if (useRight) {
            if (!"right_arm".equals(partName)) {
                return;
            }
        } else {
            if (!"left_arm".equals(partName)) {
                return;
            }
        }

        vanillaPart.xRot = -1.20F;

        if (useRight) {
            vanillaPart.yRot = -0.6F;
            vanillaPart.zRot = 0.10F;
        } else {
            vanillaPart.yRot = 0.6F;
            vanillaPart.zRot = -0.10F;
        }
    }

    @Unique
    private static boolean mus$usingRightArm(LivingEntity entity) {
        boolean mainArmRight = entity.getMainArm() == HumanoidArm.RIGHT;
        boolean usingMainHand = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
        return usingMainHand == mainArmRight;
    }
}