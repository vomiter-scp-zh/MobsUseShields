package com.vomiter.mobsuseshields.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.Optional;

public class ModCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
            Commands.literal("mus")
            .then(Commands.literal("main_hand")
            .executes(ctx -> {
                ItemStack mainStack = Optional.ofNullable(ctx.getSource().getPlayer()).map(LivingEntity::getMainHandItem).orElse(ItemStack.EMPTY);
                var tag = mainStack.save(ctx.getSource().registryAccess());
                ctx.getSource().sendSuccess(
                    () -> Component.literal(tag.getAsString()), true);
                    return 1;
            }))
        );

    }
}
