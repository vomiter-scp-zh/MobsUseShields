package com.vomiter.mobsuseshields.mixin.compat.emf;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import traben.entity_model_features.models.parts.EMFModelPartVanilla;

@Mixin(value = EMFModelPartVanilla.class, remap = false)
public interface EMFModelPartVanillaAccessor {
    @Accessor("name")
    String mus$getName();
}