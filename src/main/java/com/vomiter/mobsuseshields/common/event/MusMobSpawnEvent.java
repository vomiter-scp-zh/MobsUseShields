package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.data.MobShieldConfigManager;
import com.vomiter.mobsuseshields.data.MobShieldSpawnChanceConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

public class MusMobSpawnEvent {
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        if(!(mob.level() instanceof ServerLevel serverLevel)) return;

        // 避免覆蓋原本已有的副手物品
        if (!mob.getOffhandItem().isEmpty()) {
            return;
        }
        MobShieldSpawnChanceConfig config = MobShieldConfigManager.getSpawn(mob.getType());
        float difficulty = serverLevel.getCurrentDifficultyAt(mob.getOnPos()).getEffectiveDifficulty();
        if(difficulty < config.minDifficulty()) return;
        if(mob.getRandom().nextFloat() > config.chance()) return;

        mob.setItemSlot(EquipmentSlot.OFFHAND, config.shield().copy());
        mob.setDropChance(EquipmentSlot.OFFHAND, 0.085f);
    }
}
