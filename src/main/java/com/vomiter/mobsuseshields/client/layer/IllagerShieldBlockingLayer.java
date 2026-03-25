package com.vomiter.mobsuseshields.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.NotNull;

public class IllagerShieldBlockingLayer<T extends AbstractIllager, M extends IllagerModel<T>> extends ItemInHandLayer<T, M> {

    public IllagerShieldBlockingLayer(RenderLayerParent<T, M> p_234846_, ItemInHandRenderer p_234847_) {
        super(p_234846_, p_234847_);
    }

    @Override
    public void render(
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource buffer,
            int packedLight,
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (!entity.isUsingItem()) return;

        ItemStack using = entity.getUseItem();
        if (using.isEmpty()) return;
        if (using.getUseAnimation() != UseAnim.BLOCK) return;
        super.render(poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
    }
}