package com.vomiter.mobsuseshields.mixin.client;

import com.vomiter.mobsuseshields.client.layer.IllagerShieldBlockingLayer;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.monster.AbstractIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllagerRenderer.class)
public abstract class IllagerRendererMixin<T extends AbstractIllager> extends MobRenderer<T, IllagerModel<T>> {

    public IllagerRendererMixin(EntityRendererProvider.Context p_174304_, IllagerModel<T> p_174305_, float p_174306_) {
        super(p_174304_, p_174305_, p_174306_);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/IllagerModel;F)V",
            at = @At("TAIL")
    )
    private void mus$addShieldBlockingLayer(
            EntityRendererProvider.Context context,
            IllagerModel<T> model,
            float shadowRadius,
            CallbackInfo ci
    ) {
        this.addLayer(new IllagerShieldBlockingLayer<>(
                (IllagerRenderer<T>) (Object) this,
                context.getItemInHandRenderer()
        ));
    }
}