package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.data.MobShieldConfigManager;
import com.vomiter.mobsuseshields.data.MobShieldSpawnConfig;
import com.vomiter.mobsuseshields.data.MobShieldSpawnEntry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

public class MusMobSpawnEvent {
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        if (mob.level().isClientSide()) return;
        if (!mob.getOffhandItem().isEmpty()) return;

        var diffInstance = event.getDifficulty();
        if (diffInstance == null) return;
        float difficulty = diffInstance.getEffectiveDifficulty();

        MobShieldSpawnConfig config = MobShieldConfigManager.getSpawn(mob.getType());
        if (config == null || !config.hasEntries()) return;

        for (MobShieldSpawnEntry entry : config.shields()) {
            if (entry == null) continue;
            if (entry.shield() == null) continue;
            if (difficulty < entry.minDifficulty()) continue;
            if (mob.getRandom().nextFloat() > entry.chance()) continue;

            mob.setItemSlot(EquipmentSlot.OFFHAND, entry.shield().copy());
            mob.setDropChance(EquipmentSlot.OFFHAND, 0.085f);
            return;
        }
    }
}