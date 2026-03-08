package com.vomiter.mobsuseshields.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class ModCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        /*
        dispatcher.register(
            Commands.literal("sd")
            .then(Commands.literal("ping")
            .executes(ctx -> {
                ctx.getSource().sendSuccess(
                    () -> Component.literal("pong"), false);
                    return 1;
            }))
        );
         */
    }
}
