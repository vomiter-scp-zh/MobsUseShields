package com.vomiter.mobsuseshields.mixin.compat.emf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vomiter.mobsuseshields.MobsUseShields;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_model_features.models.animation.EMFAnimationEntityContext;
import traben.entity_model_features.models.parts.EMFModelPart;
import traben.entity_model_features.models.parts.EMFModelPartVanilla;
import traben.entity_model_features.models.parts.EMFModelPartWithState;

@Mixin(value = EMFModelPartWithState.class)
public abstract class EMFModelPartWithStateMixin {

    @Inject(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Ltraben/entity_model_features/models/parts/EMFModelPart$Animator;run()V",
            shift = At.Shift.AFTER
        )
    )
    private void mus$applyBlockingPoseAfterEmfAnimation(
        PoseStack matrices,
        com.mojang.blaze3d.vertex.VertexConsumer vertices,
        int light,
        int overlay,
        float red,
        float green,
        float blue,
        float alpha,
        CallbackInfo ci
    ) {
        if (!(((Object) this) instanceof EMFModelPartVanilla vanillaPart)) {
            return;
        }

        var emfEntity = EMFAnimationEntityContext.getEMFEntity();
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

        MobsUseShields.LOGGER.debug("[MUS] Applied EMF blocking pose to part {}", partName);
    }

    @Unique
    private static boolean mus$usingRightArm(LivingEntity entity) {
        boolean mainArmRight = entity.getMainArm() == HumanoidArm.RIGHT;
        boolean usingMainHand = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
        return usingMainHand == mainArmRight;
    }
}