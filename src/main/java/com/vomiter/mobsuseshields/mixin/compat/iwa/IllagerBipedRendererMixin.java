package com.vomiter.mobsuseshields.mixin.compat.iwa;

/*
import com.vomiter.mobsuseshields.compat.client.IWAIllagerShieldBlockingLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.monster.AbstractIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tallestegg.illagersweararmor.client.model.IllagerBipedModel;
import tallestegg.illagersweararmor.client.renderer.IllagerBipedRenderer;

@Mixin(value = IllagerBipedRenderer.class, remap = false)
public abstract class IllagerBipedRendererMixin<T extends AbstractIllager> extends MobRenderer<T, IllagerBipedModel<T>> {
    public IllagerBipedRendererMixin(EntityRendererProvider.Context p_174304_, IllagerBipedModel<T> p_174305_, float p_174306_) {
        super(p_174304_, p_174305_, p_174306_);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void mus$addShieldBlockingLayer(
            EntityRendererProvider.Context context, CallbackInfo ci
    ) {

        this.addLayer(new IWAIllagerShieldBlockingLayer<>(
                (IllagerBipedRenderer<T>) (Object) this,
                context.getItemInHandRenderer()
        ));
    }

}


 */