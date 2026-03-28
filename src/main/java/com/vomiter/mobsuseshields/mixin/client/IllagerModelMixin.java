package com.vomiter.mobsuseshields.mixin.client;

import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
            method = "setupAnim(Lnet/minecraft/world/entity/monster/AbstractIllager;FFFFF)V",
            at = @At("TAIL")
    )
    private void mus$reapplyShieldBlockPose(
            AbstractIllager entity, float par2, float par3, float par4, float par5, float par6, CallbackInfo ci
    ) {
        if (!entity.isUsingItem()) return;

        ItemStack using = entity.getUseItem();
        if (using.isEmpty()) return;
        if (using.getUseAnimation() != UseAnim.BLOCK) return;

        arms.visible = false;
        rightArm.visible = true;
        leftArm.visible = true;
        if (mus$usingRightArm(entity)) {
            this.rightArm.xRot = -0.95F;
            this.rightArm.yRot = -0.52F;
            this.rightArm.zRot = 0.0F;
        } else {
            this.leftArm.xRot = -0.95F;
            this.leftArm.yRot = 0.52F;
            this.leftArm.zRot = 0.0F;
        }
    }

    @Unique
    private static boolean mus$usingRightArm(Monster entity) {
        boolean mainArmRight = entity.getMainArm() == HumanoidArm.RIGHT;
        boolean usingMainHand = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
        return usingMainHand == mainArmRight;
    }

}
