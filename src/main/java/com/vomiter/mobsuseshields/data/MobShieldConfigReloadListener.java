package com.vomiter.mobsuseshields.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vomiter.mobsuseshields.MobsUseShields;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MobShieldConfigReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final String DIRECTORY = "mob_shield";
    public static final MobShieldConfigReloadListener INSTANCE = new MobShieldConfigReloadListener();

    private MobShieldConfigReloadListener() {
        super(GSON, DIRECTORY);
    }

    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(MobShieldConfigReloadListener.INSTANCE);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        MobShieldConfigManager.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            ResourceLocation fileId = entry.getKey();
            JsonElement json = entry.getValue();

            try {
                if (!json.isJsonObject()) {
                    throw new IllegalArgumentException("Expected JSON object");
                }

                JsonObject obj = json.getAsJsonObject();

                int useDuration = GsonHelper.getAsInt(obj, "use_duration", 60);
                int cooldownDuration = GsonHelper.getAsInt(obj, "cooldown_duration", 60);
                int checkInterval = GsonHelper.getAsInt(obj, "check_continue_to_use_interval", 30);

                MobShieldConfig config = new MobShieldConfig(
                        useDuration,
                        cooldownDuration,
                        checkInterval
                );

                MobShieldSpawnConfig spawnConfig = parseSpawnConfig(obj);

                MobShieldConfigManager.put(fileId, config);
                MobShieldConfigManager.put(fileId, spawnConfig);

                MobsUseShields.LOGGER.info("[MUS] Loaded shield config for {} -> {}", fileId, config);
            } catch (Exception e) {
                MobsUseShields.LOGGER.error("[MUS] Failed to load mob_shield config {}", fileId, e);
            }
        }

        MobsUseShields.LOGGER.info("[MUS] Loaded {} mob shield configs", map.size());
    }

    private static MobShieldSpawnConfig parseSpawnConfig(JsonObject obj) {
        if (obj.has("shields")) {
            JsonArray arr = GsonHelper.getAsJsonArray(obj, "shields");
            List<MobShieldSpawnEntry> entries = new ArrayList<>();

            for (JsonElement element : arr) {
                if (!element.isJsonObject()) {
                    throw new IllegalArgumentException("'shields' must contain only JSON objects");
                }
                entries.add(parseSpawnEntry(element.getAsJsonObject()));
            }

            if (!entries.isEmpty()) {
                return new MobShieldSpawnConfig(List.copyOf(entries));
            }
        }

        // fallback: legacy single-entry fields
        return new MobShieldSpawnConfig(List.of(parseSpawnEntry(obj)));
    }

    private static MobShieldSpawnEntry parseSpawnEntry(JsonObject obj) {
        String shieldId = GsonHelper.getAsString(obj, "shield_id", "minecraft:shield");
        Item shieldItem = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(shieldId));
        ItemStack shieldStack = shieldItem == null ? new ItemStack(Items.SHIELD) : new ItemStack(shieldItem);

        float shieldChance = GsonHelper.getAsFloat(obj, "chance", 0);
        float minDifficulty = GsonHelper.getAsFloat(obj, "min_difficulty", 2.25f);

        return new MobShieldSpawnEntry(
                shieldStack,
                shieldChance,
                minDifficulty
        );
    }
}