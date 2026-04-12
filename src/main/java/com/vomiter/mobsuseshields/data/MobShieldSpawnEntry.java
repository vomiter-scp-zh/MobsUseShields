package com.vomiter.mobsuseshields.data;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record MobShieldSpawnEntry(ItemStack shield, float chance, float minDifficulty) {
    public static final MobShieldSpawnEntry DEFAULT =
            new MobShieldSpawnEntry(
                    new ItemStack(Items.SHIELD),
                    0,
                    2.25f
            );
}