package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.MobsUseShields;
import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import com.vomiter.mobsuseshields.common.entity.ai.MobUseShieldAttackGoal;
import com.vomiter.mobsuseshields.common.entity.ai.MobUseShieldGoal;
import com.vomiter.mobsuseshields.mixin.MeleeAttackGoalAccessor;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.ArrayList;
import java.util.List;
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
        List<WrappedGoal> listToRemove = new ArrayList<>();
        int firstPriority = 100;
        for (WrappedGoal availableGoal : mob.goalSelector.getAvailableGoals()) {
            if(availableGoal.getGoal() instanceof MeleeAttackGoal meleeAttackGoal){
                if(!(meleeAttackGoal instanceof MobUseShieldAttackGoal)){
                    listToRemove.add(availableGoal);
                }
                firstPriority = Math.min(firstPriority, availableGoal.getPriority());
            }
        }
        for (WrappedGoal wrappedGoal : listToRemove) {
            mob.goalSelector.removeGoal(wrappedGoal.getGoal());
            mob.goalSelector.addGoal(wrappedGoal.getPriority(), new MobUseShieldAttackGoal((MeleeAttackGoalAccessor) wrappedGoal.getGoal()));
            //wrap the goal to make it not usable when it should use shield
        }
        if(!mobUseShield.mus$shieldGoalsInjected()){
            mob.goalSelector.addGoal(firstPriority, new MobUseShieldGoal(mob, mobUseShield));
            mobUseShield.mus$setShieldGoalsInjected(true);
        }

        MobsUseShields.LOGGER.info("Replaced Attack Goal");
        MobsUseShields.LOGGER.info("Current Goals: {}", mob.goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal).collect(Collectors.toSet()));
    }
}
