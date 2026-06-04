package com.vomiter.mobsuseshields.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public record MobShieldSpawnEntry(Function<HolderLookup.Provider, ItemStack> shield, float chance, float minDifficulty) {
    public static final MobShieldSpawnEntry DEFAULT =
            new MobShieldSpawnEntry(
                    (ra) -> new ItemStack(Items.SHIELD),
                    0,
                    2.25f
            );
}