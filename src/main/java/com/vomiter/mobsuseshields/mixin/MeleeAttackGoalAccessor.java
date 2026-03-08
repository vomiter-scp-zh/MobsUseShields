package com.vomiter.mobsuseshields.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MeleeAttackGoal.class)
public interface MeleeAttackGoalAccessor {
    @Accessor("mob")
    PathfinderMob getMob();

    @Accessor("speedModifier")
    double getSpeedModifier();

    @Accessor("followingTargetEvenIfNotSeen")
    boolean getFollowOption();
}
