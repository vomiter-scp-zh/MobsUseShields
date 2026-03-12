package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.MobsUseShields;
import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import com.vomiter.mobsuseshields.common.entity.ai.MobUseShieldAttackGoal;
import com.vomiter.mobsuseshields.common.entity.ai.MobUseShieldGoal;
import com.vomiter.neurolib.common.entity.ai.GoalMutateUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EquipShieldEvent {
    static void onMobEquipShield(LivingEquipmentChangeEvent event){
        LivingEntity entity = event.getEntity();
        if(!(entity instanceof Mob mob)) return;
        if(!(entity instanceof ICanUseShieldMob mobUseShield)) return;
        mobUseShield.mus$setCanUseShield(true); //debugging
        if(!mobUseShield.mus$canUseShield()) return;
        if(!event.getSlot().equals(EquipmentSlot.OFFHAND)) return;
        if(!(event.getTo().getItem() instanceof ShieldItem)) return;
        int firstPriority = GoalMutateUtils.replaceAllMeleeWithMutated(
                mob.goalSelector,
                MobUseShieldAttackGoal::new,
                new ArrayList<>()
        );

        if(!mobUseShield.mus$shieldGoalsInjected()){
            mob.goalSelector.addGoal(firstPriority, new MobUseShieldGoal(mob, mobUseShield));
            mobUseShield.mus$setShieldGoalsInjected(true);
        }

        MobsUseShields.LOGGER.info("[MUS] Replaced Attack Goal");
        MobsUseShields.LOGGER.info("[MUS] Current Goals: {}", mob.goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal).collect(Collectors.toSet()));
    }
}
