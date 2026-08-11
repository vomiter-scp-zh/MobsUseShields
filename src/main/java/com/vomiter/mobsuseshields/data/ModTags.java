package com.vomiter.mobsuseshields.data;

import com.vomiter.mobsuseshields.MobsUseShields;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModTags {
    public static TagKey<EntityType<?>> DISABLE_SHIELD
            = TagKey.create(
                    BuiltInRegistries.ENTITY_TYPE.key(),
                    ResourceLocation.fromNamespaceAndPath(
                            MobsUseShields.MOD_ID,
                            "disable_shield"
                    )
            );
}
