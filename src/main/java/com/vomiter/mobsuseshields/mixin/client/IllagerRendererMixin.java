package com.vomiter.mobsuseshields.mixin.client;

import com.vomiter.mobsuseshields.client.layer.IllagerShieldBlockingLayer;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllagerRenderer.class)
public abstract class IllagerRendererMixin <T extends AbstractIllager, S extends IllagerRenderState> extends MobRenderer<T, S, IllagerModel<S>> {


    public IllagerRendererMixin(EntityRendererProvider.Context p_174304_, IllagerModel<S> p_174305_, float p_174306_) {
        super(p_174304_, p_174305_, p_174306_);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void mus$addShieldBlockingLayer(
            EntityRendererProvider.Context context,
            IllagerModel model,
            float shadowRadius,
            CallbackInfo ci
    ) {

        this.addLayer((RenderLayer<S, IllagerModel<S>>) (Object)new IllagerShieldBlockingLayer((RenderLayerParent<IllagerRenderState, IllagerModel<IllagerRenderState>>)(Object) this));
    }
}