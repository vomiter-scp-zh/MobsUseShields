package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.data.MobShieldConfigManager;
import com.vomiter.mobsuseshields.data.MobShieldSpawnConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

public class MusMobSpawnEvent {
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        var mob = event.getEntity();
        if (mob.level().isClientSide()) return;
        if (!mob.getOffhandItem().isEmpty()) return;

        var diffInstance = event.getDifficulty();
        if (diffInstance == null) return;
        float difficulty = diffInstance.getEffectiveDifficulty();

        MobShieldSpawnConfig config = MobShieldConfigManager.getSpawn(mob.getType());
        var shield = config.selectShield(difficulty, mob.getRandom(), mob.registryAccess());
        if (shield.isEmpty()) return;
        mob.setItemSlot(EquipmentSlot.OFFHAND, shield);
        mob.setDropChance(EquipmentSlot.OFFHAND, 0.085f);
    }
}
