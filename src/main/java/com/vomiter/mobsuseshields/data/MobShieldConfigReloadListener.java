package com.vomiter.mobsuseshields.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.vomiter.mobsuseshields.MobsUseShields;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MobShieldConfigReloadListener extends SimpleJsonResourceReloadListener<Dynamic<?>> {
    private static final String DIRECTORY = "mob_shield";

    public static final MobShieldConfigReloadListener INSTANCE =
            new MobShieldConfigReloadListener();

    private MobShieldConfigReloadListener() {
        super(
                Codec.PASSTHROUGH,
                FileToIdConverter.json(DIRECTORY)
        );
    }

    public static void onAddReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(
                Identifier.fromNamespaceAndPath(MobsUseShields.MOD_ID, "mob_shield"),
                MobShieldConfigReloadListener.INSTANCE
        );
    }

    @Override
    protected void apply(
            Map<Identifier, Dynamic<?>> map,
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller profiler
    ) {
        MobShieldConfigManager.clear();

        HolderLookup.Provider registries = getRegistryLookup();

        int loaded = 0;

        for (Map.Entry<Identifier, Dynamic<?>> entry : map.entrySet()) {
            Identifier fileId = entry.getKey();
            JsonElement json = entry.getValue()
                    .convert(JsonOps.INSTANCE)
                    .getValue();

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

                MobShieldSpawnConfig spawnConfig = parseSpawnConfig(obj, registries);

                MobShieldConfigManager.put(fileId, config);
                MobShieldConfigManager.put(fileId, spawnConfig);

                loaded++;

                MobsUseShields.LOGGER.info(
                        "[MUS] Loaded shield config for {} -> {}",
                        fileId,
                        config
                );
            } catch (Exception e) {
                MobsUseShields.LOGGER.error(
                        "[MUS] Failed to load mob_shield config {}",
                        fileId,
                        e
                );
            }
        }

        MobsUseShields.LOGGER.info(
                "[MUS] Loaded {} mob shield configs",
                loaded
        );
    }

    private static MobShieldSpawnConfig parseSpawnConfig(
            JsonObject obj,
            HolderLookup.Provider registries
    ) {
        if (obj.has("shields")) {
            JsonArray arr = GsonHelper.getAsJsonArray(obj, "shields");
            List<MobShieldSpawnEntry> entries = new ArrayList<>();

            for (JsonElement element : arr) {
                if (!element.isJsonObject()) {
                    throw new IllegalArgumentException("'shields' must contain only JSON objects");
                }

                entries.add(parseSpawnEntry(element.getAsJsonObject(), registries));
            }

            if (!entries.isEmpty()) {
                return new MobShieldSpawnConfig(List.copyOf(entries));
            }
        }

        return new MobShieldSpawnConfig(List.of(parseSpawnEntry(obj, registries)));
    }

    private static MobShieldSpawnEntry parseSpawnEntry(
            JsonObject obj,
            HolderLookup.Provider registries
    ) {
        String shieldId = GsonHelper.getAsString(
                obj,
                "shield_id",
                "minecraft:shield"
        );

        Identifier shieldIdentifier = Identifier.tryParse(shieldId);

        Item shieldItem = shieldIdentifier == null
                ? Items.SHIELD
                : BuiltInRegistries.ITEM.getValue(shieldIdentifier);

        if (shieldItem == Items.AIR) {
            MobsUseShields.LOGGER.warn(
                    "[MUS] Unknown shield_id {}, fallback to minecraft:shield",
                    shieldId
            );
            shieldItem = Items.SHIELD;
        }

        ItemStack fallbackStack = new ItemStack(shieldItem);

        Function<HolderLookup.Provider, ItemStack> shieldStackGetter = lookup -> {
            if (obj.has("shield_stack")) {
                JsonElement stackJson = obj.get("shield_stack");

                try {
                    ItemStack stack = parseItemStack(lookup, stackJson);

                    if (!stack.isEmpty()) {
                        return stack;
                    }
                } catch (Exception e) {
                    MobsUseShields.LOGGER.error(
                            "[MUS] Failed to load shield_stack from {}",
                            stackJson,
                            e
                    );
                }
            }

            return fallbackStack.copy();
        };

        float shieldChance = GsonHelper.getAsFloat(obj, "chance", 0.0f);
        float minDifficulty = GsonHelper.getAsFloat(obj, "min_difficulty", 2.25f);

        return new MobShieldSpawnEntry(
                shieldStackGetter,
                shieldChance,
                minDifficulty
        );
    }

    private static ItemStack parseItemStack(
            HolderLookup.Provider registries,
            JsonElement json
    ) {
        RegistryOps<@NotNull JsonElement> ops =
                registries.createSerializationContext(JsonOps.INSTANCE);

        return ItemStack.CODEC
                .parse(ops, json)
                .resultOrPartial(error -> MobsUseShields.LOGGER.error(
                        "[MUS] Failed to parse ItemStack: {}",
                        error
                ))
                .orElse(ItemStack.EMPTY);
    }

}