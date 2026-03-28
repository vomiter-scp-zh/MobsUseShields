package com.vomiter.mobsuseshields.mixin.client;

import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractZombieModel.class)
public abstract class AbstractZombieModelMixin<T extends Monster> extends HumanoidModel<T> {
    private AbstractZombieModelMixin(ModelPart p_170677_) {
        super(p_170677_);
    }

    @Inject(
        method = "setupAnim(Lnet/minecraft/world/entity/monster/Monster;FFFFF)V",
        at = @At("TAIL")
    )
    private void mus$reapplyShieldBlockPose(
        T entity,
        float limbSwing,
        float limbSwingAmount,
        float ageInTicks,
        float netHeadYaw,
        float headPitch,
        CallbackInfo ci
    ) {
        if (!entity.isUsingItem()) return;

        ItemStack using = entity.getUseItem();
        if (using.isEmpty()) return;
        if (using.getUseAnimation() != UseAnim.BLOCK) return;

        if (mus$usingRightArm(entity)) {
            // 直接覆蓋，不沿用前面動畫留下來的值
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