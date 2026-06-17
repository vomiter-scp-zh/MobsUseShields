package com.vomiter.mobsuseshields.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vomiter.mobsuseshields.ClientConfig;
import com.vomiter.mobsuseshields.client.ClientEventHandler;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import org.jetbrains.annotations.NotNull;

public class IllagerShieldBlockingLayer extends ItemInHandLayer<
        @NotNull IllagerRenderState,
        @NotNull IllagerModel<@NotNull IllagerRenderState>
        > {

    public IllagerShieldBlockingLayer(
            RenderLayerParent<@NotNull IllagerRenderState, @NotNull IllagerModel<@NotNull IllagerRenderState>> parent
    ) {
        super(parent);
    }

    @Override
    public void submit(
            @NotNull PoseStack poseStack,
            @NotNull SubmitNodeCollector collector,
            int packedLight,
            IllagerRenderState renderState,
            float yRot,
            float xRot
    ) {
        boolean offhandShield = renderState.getRenderDataOrDefault(
                ClientEventHandler.OFFHAND_SHIELD,
                false
        );

        boolean usingShield = renderState.getRenderDataOrDefault(
                ClientEventHandler.USING_SHIELD,
                false
        );

        if (!ClientConfig.HIDE_PILLAGER_SHIELD_IN_ARMS && offhandShield) {
            super.submit(poseStack, collector, packedLight, renderState, yRot, xRot);
            return;
        }

        if (usingShield) {
            super.submit(poseStack, collector, packedLight, renderState, yRot, xRot);
        }
    }
}