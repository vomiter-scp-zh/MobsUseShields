package com.vomiter.mobsuseshields.mixin.compat.emf;

import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import traben.entity_model_features.models.parts.EMFModelPart;
import traben.entity_model_features.models.parts.EMFModelPartVanilla;

import java.util.List;
import java.util.Map;

@Mixin(value = EMFModelPartVanilla.class, remap = false)
public abstract class EMFModelPartVanillaMixin extends EMFModelPart{

    private EMFModelPartVanillaMixin(List<Cube> cuboids, Map<String, ModelPart> children) {
        super(cuboids, children);
    }
/*

    @Inject(
        method = "render",
        at = @At(
            value = "TAIL"
        ),
        remap = true
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
        MobsUseShields.LOGGER.info("[MUS] Trying to take over EMF");
        if (!(((Object) this) instanceof EMFModelPartRoot root)) {

            return;
        }

        var emfEntity = EMFAnimationEntityContext.getEMFEntity();
        if (!(emfEntity instanceof LivingEntity entity)) {
            MobsUseShields.LOGGER.info("[MUS] Failed 2");
            return;
        }

        if (!(entity instanceof Mob)) {
            MobsUseShields.LOGGER.info("[MUS] Failed 3");

            return;
        }

        if (!entity.isUsingItem()) {
            MobsUseShields.LOGGER.info("[MUS] Failed 4");

            return;
        }

        ItemStack using = entity.getUseItem();
        if (using.isEmpty() || using.getUseAnimation() != UseAnim.BLOCK) {
            MobsUseShields.LOGGER.info("[MUS] Failed 5");

            return;
        }

        Map<String, EMFModelPartVanilla> parts =
            ((EMFModelPartRootAccessor) root).mus$getAllVanillaParts();

        boolean useRight = mus$usingRightArm(entity);
        String armKey = useRight ? "right_arm" : "left_arm";

        EMFModelPartVanilla arm = parts.get(armKey);
        if (arm == null) {
            MobsUseShields.LOGGER.info("[MUS] Failed 6");

            MobsUseShields.LOGGER.info("[MUS] EMF arm part not found. armKey = {}, entity = {}", armKey, entity);
            return;
        }

        if (useRight) {
            arm.xRot = -1.20F;
            arm.yRot = -0.35F;
            arm.zRot = 0.10F;
        } else {
            arm.xRot = -1.20F;
            arm.yRot = 0.35F;
            arm.zRot = -0.10F;
        }

        MobsUseShields.LOGGER.info("[MUS] Applied EMF blocking pose to {}", armKey);
    }

    @Unique
    private static boolean mus$usingRightArm(LivingEntity entity) {
        boolean mainArmRight = entity.getMainArm() == HumanoidArm.RIGHT;
        boolean usingMainHand = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
        return usingMainHand == mainArmRight;
    }

 */
}