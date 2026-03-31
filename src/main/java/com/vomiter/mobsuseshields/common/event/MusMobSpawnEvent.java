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
        if (mob.level().isClientSide()) return;
        if (!mob.getOffhandItem().isEmpty()) {
            return;
        }

        MobShieldSpawnChanceConfig config = MobShieldConfigManager.getSpawn(mob.getType());

        if(config == null) return;
        var diffInstance = event.getDifficulty();
        if(diffInstance == null) return;
        float difficulty = diffInstance.getEffectiveDifficulty();
        if(difficulty < config.minDifficulty()) return;
        if(mob.getRandom().nextFloat() > config.chance()) return;
        if(config.shield() == null) return;
        mob.setItemSlot(EquipmentSlot.OFFHAND, config.shield().copy());
        mob.setDropChance(EquipmentSlot.OFFHAND, 0.085f);
    }
}
