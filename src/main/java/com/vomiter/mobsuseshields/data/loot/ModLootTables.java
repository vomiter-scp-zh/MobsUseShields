package com.vomiter.mobsuseshields.data.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;

public class ModLootTables extends LootTableProvider {
    public ModLootTables(PackOutput packOutput) {
        super(packOutput, Collections.emptySet(), List.of(
                new SubProviderEntry(SubLootTable::new, LootContextParamSets.ALL_PARAMS)
        ));
    }
}
