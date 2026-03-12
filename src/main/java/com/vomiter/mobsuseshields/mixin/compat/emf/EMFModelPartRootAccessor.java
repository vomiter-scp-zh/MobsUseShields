package com.vomiter.mobsuseshields.mixin.compat.emf;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import traben.entity_model_features.models.parts.EMFModelPartRoot;
import traben.entity_model_features.models.parts.EMFModelPartVanilla;

import java.util.Map;

@Mixin(value = EMFModelPartRoot.class, remap = false)
public interface EMFModelPartRootAccessor {
    @Accessor("allVanillaParts")
    Map<String, EMFModelPartVanilla> mus$getAllVanillaParts();
}