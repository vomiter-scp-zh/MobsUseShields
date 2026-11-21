package com.vomiter.mobsuseshields.data;

import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vomiter.mobsuseshields.MobsUseShields;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobShieldConfigReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, MobShieldConfigReloadListener.Settings>> {
    private static final FileToIdConverter FILES = FileToIdConverter.json("mob_shield");
    public static final MobShieldConfigReloadListener INSTANCE = new MobShieldConfigReloadListener();

    private MobShieldConfigReloadListener() {
    }

    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(MobShieldConfigReloadListener.INSTANCE);
    }

    public record Settings(MobShieldConfig behavior, MobShieldSpawnConfig spawn) {}

    @Override
    protected @NotNull Map<ResourceLocation, Settings> prepare(@NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        Map<ResourceLocation, Settings> settings = new HashMap<>();
        for (var stack : FILES.listMatchingResourceStacks(manager).entrySet()) {
            ResourceLocation entityId = FILES.fileToId(stack.getKey());
            for (Resource resource : stack.getValue()) {
                try (var reader = resource.openAsReader()) {
                    JsonObject layer = JsonParser.parseReader(reader).getAsJsonObject();
                    settings.put(entityId, mergeLayer(settings.get(entityId), layer));
                } catch (IOException | RuntimeException e) {
                    MobsUseShields.LOGGER.error("Skipping mob_shield layer {} from {}", entityId, resource.sourcePackId(), e);
                }
            }
        }
        return settings;
    }

    @Override
    protected void apply(Map<ResourceLocation, Settings> settings, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        MobShieldConfigManager.clear();
        settings.forEach((id, value) -> {
            MobShieldConfigManager.put(id, value.behavior());
            MobShieldConfigManager.put(id, value.spawn());
        });
    }

    static Settings mergeLayer(Settings previous, JsonObject obj) {
        JsonArray entries = GsonHelper.getAsJsonArray(obj, "shields", new JsonArray());
        boolean weighted = obj.has("spawn_chance") || obj.has("replace") || entries.isEmpty();
        for (JsonElement entry : entries) {
            weighted |= entry.getAsJsonObject().has("weight");
        }

        MobShieldConfig base = weighted && previous != null ? previous.behavior() : MobShieldConfig.DEFAULT;
        MobShieldConfig behavior = new MobShieldConfig(
                GsonHelper.getAsInt(obj, "use_duration", base.useDuration()),
                GsonHelper.getAsInt(obj, "cooldown_duration", base.cooldownDuration()),
                GsonHelper.getAsInt(obj, "check_continue_to_use_interval", base.checkContinueToUseInterval())
        );
        if (!weighted) return new Settings(behavior, parseSpawnConfig(obj));

        if (obj.has("chance") || obj.has("shield_id") || obj.has("shield_stack")) {
            throw new IllegalArgumentException("Weighted shield entries belong in the shields list");
        }
        boolean replace = GsonHelper.getAsBoolean(obj, "replace", false);
        List<MobShieldSpawnEntry> pool = new ArrayList<>();
        double chance = 1;
        if (!replace && previous != null) {
            if (previous.spawn().spawnChance() == null) {
                throw new IllegalArgumentException("Use replace: true to replace a legacy shield config");
            }
            pool.addAll(previous.spawn().shields());
            chance = previous.spawn().spawnChance();
        }
        chance = GsonHelper.getAsDouble(obj, "spawn_chance", chance);
        if (!Double.isFinite(chance) || chance < 0 || chance > 1) {
            throw new IllegalArgumentException("spawn_chance must be between 0 and 1");
        }
        for (JsonElement element : entries) {
            JsonObject entry = element.getAsJsonObject();
            double weight = GsonHelper.getAsDouble(entry, "weight");
            if (entry.has("chance") || !Double.isFinite(weight) || weight < 0) {
                throw new IllegalArgumentException("Weighted entries require a finite nonnegative weight and no chance");
            }
            pool.add(parseSpawnEntry(entry, weight));
        }
        return new Settings(behavior, new MobShieldSpawnConfig(List.copyOf(pool), chance));
    }

    private static MobShieldSpawnConfig parseSpawnConfig(JsonObject obj) {
        if (obj.has("shields")) {
            JsonArray arr = GsonHelper.getAsJsonArray(obj, "shields");
            List<MobShieldSpawnEntry> entries = new ArrayList<>();

            for (JsonElement element : arr) {
                if (!element.isJsonObject()) {
                    throw new IllegalArgumentException("'shields' must contain only JSON objects");
                }
                entries.add(parseSpawnEntry(element.getAsJsonObject(), null));
            }

            if (!entries.isEmpty()) {
                return new MobShieldSpawnConfig(List.copyOf(entries));
            }
        }

        // fallback: legacy single-entry fields
        return new MobShieldSpawnConfig(List.of(parseSpawnEntry(obj, null)));
    }

    private static MobShieldSpawnEntry parseSpawnEntry(JsonObject obj, Double weight) {
        String shieldId = GsonHelper.getAsString(obj, "shield_id", "minecraft:shield");
        ResourceLocation id = ResourceLocation.tryParse(shieldId);
        if (weight != null && (id == null || !ForgeRegistries.ITEMS.containsKey(id))) {
            throw new IllegalArgumentException("Unknown shield_id: " + shieldId);
        }
        Item shieldItem = ForgeRegistries.ITEMS.getValue(id);
        ItemStack shieldStack = shieldItem == null ? new ItemStack(Items.SHIELD) : new ItemStack(shieldItem);
        if(obj.has("shield_stack")){
            try {
                var shieldStack1 = ItemStack.of(TagParser.parseTag(obj.get("shield_stack").getAsString()));
                if(shieldStack1.isEmpty()){
                    if (weight != null) throw new IllegalArgumentException("Empty shield_stack");
                    MobsUseShields.LOGGER.error("[Mobs Use Shield] shield stack is empty, fall back to {}", shieldId);
                }
                shieldStack = shieldStack1;
            } catch (CommandSyntaxException e) {
                if (weight != null) throw new IllegalArgumentException("Invalid shield_stack", e);
                MobsUseShields.LOGGER.error("[Mobs Use Shield] Failed to parse shield stack for {}, fallback to {}", obj.get("shield_stack"), shieldId);
            }
        }

        float shieldChance = GsonHelper.getAsFloat(obj, "chance", 0);
        float minDifficulty = GsonHelper.getAsFloat(obj, "min_difficulty", 2.25f);

        if (weight != null && !Float.isFinite(minDifficulty)) {
            throw new IllegalArgumentException("min_difficulty must be finite");
        }
        return new MobShieldSpawnEntry(
                shieldStack,
                shieldChance,
                minDifficulty,
                weight
        );
    }
}
