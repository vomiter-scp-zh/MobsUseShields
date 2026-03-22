package com.vomiter.mobsuseshields.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vomiter.mobsuseshields.MobsUseShields;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;

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

                // SimpleJsonResourceReloadListener 讀到的 key 會是：
                // minecraft:zombie  <- 來自 data/minecraft/mob_shield/zombie.json
                // yourmod:foo/bar   <- 來自 data/yourmod/mob_shield/foo/bar.json
                MobShieldConfigManager.put(fileId, config);

                MobsUseShields.LOGGER.info("[MUS] Loaded shield config for {} -> {}", fileId, config);
            } catch (Exception e) {
                MobsUseShields.LOGGER.error("[MUS] Failed to load mob_shield config {}", fileId, e);
            }
        }

        MobsUseShields.LOGGER.info("[MUS] Loaded {} mob shield configs", map.size());
    }
}