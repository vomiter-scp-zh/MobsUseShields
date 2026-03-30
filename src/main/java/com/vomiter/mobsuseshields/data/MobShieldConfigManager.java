package com.vomiter.mobsuseshields.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MobShieldConfigManager {
    private static final Map<ResourceLocation, MobShieldConfig> CONFIGS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, MobShieldSpawnChanceConfig> SPAWN_CONFIGS = new ConcurrentHashMap<>();

    private MobShieldConfigManager() {
    }

    public static void clear() {
        CONFIGS.clear();
    }

    public static void put(ResourceLocation entityId, MobShieldConfig config) {
        CONFIGS.put(entityId, config);
    }
    public static void put(ResourceLocation entityId, MobShieldSpawnChanceConfig config) {SPAWN_CONFIGS.put(entityId, config);}

    public static MobShieldConfig get(ResourceLocation entityId) {
        return CONFIGS.getOrDefault(entityId, MobShieldConfig.DEFAULT);
    }

    public static MobShieldConfig get(EntityType<?> type) {
        ResourceLocation key = EntityType.getKey(type);
        return key != null ? get(key) : MobShieldConfig.DEFAULT;
    }

    public static MobShieldSpawnChanceConfig getSpawn(ResourceLocation entityId) {
        return SPAWN_CONFIGS.getOrDefault(entityId, MobShieldSpawnChanceConfig.DEFAULT);
    }

    public static MobShieldSpawnChanceConfig getSpawn(EntityType<?> type) {
        ResourceLocation key = EntityType.getKey(type);
        return key != null ? getSpawn(key) : MobShieldSpawnChanceConfig.DEFAULT;
    }


}