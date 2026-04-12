package com.vomiter.mobsuseshields.data;

import java.util.List;

public record MobShieldSpawnConfig(List<MobShieldSpawnEntry> shields) {
    public static final MobShieldSpawnConfig DEFAULT =
            new MobShieldSpawnConfig(List.of(MobShieldSpawnEntry.DEFAULT));

    public boolean hasEntries() {
        return shields != null && !shields.isEmpty();
    }
}