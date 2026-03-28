package com.vomiter.mobsuseshields.mixin.client;

import net.minecraft.client.model.HumanoidModel;
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

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {

    @Shadow public HumanoidModel.ArmPose leftArmPose;
    @Shadow public HumanoidModel.ArmPose rightArmPose;

    @Inject(
        method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
        at = @At("HEAD")
    )
    private void mus$applyBlockUsePose(
        T entity,
        float limbSwing,
        float limbSwingAmount,
        float ageInTicks,
        float netHeadYaw,
        float headPitch,
        CallbackInfo ci
    ) {
        if (!(entity instanceof Mob)) {
            return;
        }

        if (!entity.isUsingItem()) {
            return;
        }

        ItemStack using = entity.getUseItem();
        if (using.isEmpty()) {
            return;
        }

        if (using.getUseAnimation() != UseAnim.BLOCK) {
            return;
        }

        if (mus$usingRightArm(entity)) {
            this.rightArmPose = HumanoidModel.ArmPose.BLOCK;
        } else {
            this.leftArmPose = HumanoidModel.ArmPose.BLOCK;
        }
    }

    @Unique
    private static boolean mus$usingRightArm(LivingEntity entity) {
        boolean mainArmRight = entity.getMainArm() == HumanoidArm.RIGHT;
        boolean usingMainHand = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
        return usingMainHand == mainArmRight;
    }
}