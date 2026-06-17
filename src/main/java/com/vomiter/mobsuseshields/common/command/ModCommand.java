package com.vomiter.mobsuseshields.common.command;

import com.google.gson.JsonElement;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.JsonOps;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ModCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("mus")
                        .then(Commands.literal("main_hand")
                                .executes(ctx -> {
                                    ItemStack mainStack = Optional.ofNullable(ctx.getSource().getPlayer())
                                            .map(LivingEntity::getMainHandItem)
                                            .orElse(ItemStack.EMPTY);

                                    RegistryOps<JsonElement> ops = ctx.getSource()
                                            .registryAccess()
                                            .createSerializationContext(JsonOps.INSTANCE);

                                    JsonElement json = ItemStack.CODEC
                                            .encodeStart(ops, mainStack)
                                            .getOrThrow(error -> new IllegalArgumentException(
                                                    "Failed to encode ItemStack: " + error
                                            ));

                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal(json.toString()),
                                            true
                                    );

                                    return 1;
                                })
                        )
        );
    }
}