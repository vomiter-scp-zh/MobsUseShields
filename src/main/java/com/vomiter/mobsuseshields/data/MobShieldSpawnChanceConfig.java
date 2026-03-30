package com.vomiter.mobsuseshields.data;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record MobShieldSpawnChanceConfig(ItemStack shield, float chance, float minDifficulty) {
    public static final MobShieldSpawnChanceConfig DEFAULT
            = new MobShieldSpawnChanceConfig(
                    new ItemStack(Items.SHIELD),
            0,
            2.25f
    );
}
