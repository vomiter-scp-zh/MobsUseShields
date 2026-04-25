package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.MobsUseShields;
import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import com.vomiter.mobsuseshields.common.entity.ai.MobUseShieldAttackGoal;
import com.vomiter.mobsuseshields.common.entity.ai.MobUseShieldGoal;
import com.vomiter.mobsuseshields.data.MobShieldConfig;
import com.vomiter.mobsuseshields.data.MobShieldConfigManager;
import com.vomiter.neurolib.common.entity.generic.GoalMutateUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ShieldItem;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.ArrayList;

public class EquipShieldEvent {
    static void onMobEquipShield(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Mob mob)) return;
        if (!(entity instanceof ICanUseShieldMob mobUseShield)) return;
        if (!event.getSlot().equals(EquipmentSlot.OFFHAND)) return;
        if (!(event.getTo().getItem() instanceof ShieldItem)) return;

        mobUseShield.mus$setCanUseShield(true);
        if (!mobUseShield.mus$canUseShield()) return;

        int firstPriority = GoalMutateUtils.replaceAllMeleeWithMutated(
                mob.goalSelector,
                MobUseShieldAttackGoal::new,
                new ArrayList<>()
        );

        if (!mobUseShield.mus$shieldGoalsInjected()) {
            mob.goalSelector.addGoal(
                    firstPriority,
                    new MobUseShieldGoal(mob, mobUseShield)
            );

            mobUseShield.mus$setShieldGoalsInjected(true);

            MobShieldConfig config = MobShieldConfigManager.get(mob.getType());
            MobsUseShields.LOGGER.debug(
                    "[MUS] Injected shield goal for {} with default config {}",
                    mob.getType(),
                    config
            );
        }

        /*
        MobsUseShields.LOGGER.debug("[MUS] Replaced Attack Goal");
        MobsUseShields.LOGGER.debug(
                "[MUS] Current Goals: {}",
                mob.goalSelector.getAvailableGoals().stream()
                        .map(WrappedGoal::getGoal)
                        .collect(Collectors.toSet())
        );
         */
    }
}