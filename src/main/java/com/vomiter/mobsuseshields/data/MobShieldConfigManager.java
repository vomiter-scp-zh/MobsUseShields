package com.vomiter.mobsuseshields.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MobShieldConfigManager {
    private static final Map<ResourceLocation, MobShieldConfig> CONFIGS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, MobShieldSpawnConfig> SPAWN_CONFIGS = new ConcurrentHashMap<>();

    private MobShieldConfigManager() {
    }

    public static void clear() {
        CONFIGS.clear();
        SPAWN_CONFIGS.clear();
    }

    public static void put(ResourceLocation entityId, MobShieldConfig config) {
        CONFIGS.put(entityId, config);
    }

    public static void put(ResourceLocation entityId, MobShieldSpawnConfig config) {
        SPAWN_CONFIGS.put(entityId, config);
    }

    public static MobShieldConfig get(ResourceLocation entityId) {
        return CONFIGS.getOrDefault(entityId, MobShieldConfig.DEFAULT);
    }

    public static MobShieldConfig get(EntityType<?> type) {
        ResourceLocation key = EntityType.getKey(type);
        return key != null ? get(key) : MobShieldConfig.DEFAULT;
    }

    public static MobShieldSpawnConfig getSpawn(ResourceLocation entityId) {
        return SPAWN_CONFIGS.getOrDefault(entityId, MobShieldSpawnConfig.DEFAULT);
    }

    public static MobShieldSpawnConfig getSpawn(EntityType<?> type) {
        ResourceLocation key = EntityType.getKey(type);
        return key != null ? getSpawn(key) : MobShieldSpawnConfig.DEFAULT;
    }
}