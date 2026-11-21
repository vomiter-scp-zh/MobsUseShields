package com.vomiter.mobsuseshields.data;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record MobShieldSpawnConfig(List<MobShieldSpawnEntry> shields, Double spawnChance) {
    public static final MobShieldSpawnConfig DEFAULT = new MobShieldSpawnConfig(List.of());

    public MobShieldSpawnConfig(List<MobShieldSpawnEntry> shields) {
        this(shields, null);
    }

    public boolean hasEntries() {
        return !shields.isEmpty();
    }

    public ItemStack selectShield(float difficulty, RandomSource random) {
        if (spawnChance == null) {
            for (var entry : shields) {
                if (difficulty >= entry.minDifficulty() && random.nextFloat() <= entry.chance()) {
                    return entry.shield().copy();
                }
            }
            return ItemStack.EMPTY;
        }

        if (shields.isEmpty() || random.nextDouble() >= spawnChance) return ItemStack.EMPTY;
        double largest = 0;
        for (var entry : shields) {
            if (difficulty >= entry.minDifficulty()) largest = Math.max(largest, entry.weight());
        }
        if (largest == 0) return ItemStack.EMPTY;

        // Scaling keeps the sum finite even when several weights approach Double.MAX_VALUE.
        double total = 0;
        for (var entry : shields) {
            if (difficulty >= entry.minDifficulty()) total += entry.weight() / largest;
        }
        double roll = random.nextDouble() * total;
        ItemStack last = ItemStack.EMPTY;
        for (var entry : shields) {
            if (difficulty < entry.minDifficulty() || entry.weight() == 0) continue;
            last = entry.shield();
            roll -= entry.weight() / largest;
            if (roll < 0) break;
        }
        return last.copy();
    }
}
