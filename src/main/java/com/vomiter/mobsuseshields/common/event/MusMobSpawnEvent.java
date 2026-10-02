package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.data.MobShieldConfigManager;
import com.vomiter.mobsuseshields.data.MobShieldSpawnConfig;
import com.vomiter.mobsuseshields.data.MobShieldSpawnEntry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

public class MusMobSpawnEvent {
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        var entity = event.getEntity();
        if (!(entity instanceof Mob mob)) return;
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

            mob.setItemSlot(EquipmentSlot.OFFHAND, entry.shield().apply(mob.registryAccess()).copy());
            mob.setDropChance(EquipmentSlot.OFFHAND, 0.085f);
            return;
        }
    }
}