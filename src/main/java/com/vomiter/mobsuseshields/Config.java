package com.vomiter.mobsuseshields;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = MobsUseShields.MOD_ID)
public class Config {
    public static boolean MOB_CONSUME_SHIELD_DURABILITY = false;
    public static boolean MOB_ALWAYS_ANTICIPATE = false;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue MOB_CONSUME_SHIELD_DURABILITY_CONFIG;
    private static final ModConfigSpec.BooleanValue MOB_ALWAYS_ANTICIPATE_CONFIG;

    static {
        BUILDER.push("general");

        MOB_CONSUME_SHIELD_DURABILITY_CONFIG = BUILDER
                .comment("If true, mobs using shields will consume shield durability when blocking.")
                .define("mobConsumeShieldDurability", false);

        MOB_ALWAYS_ANTICIPATE_CONFIG = BUILDER
                .comment("If true, shield-using mobs will always use anticipation logic instead of relying on dynamic conditions.")
                .define("mobAlwaysAnticipate", false);

        BUILDER.pop();
    }

    static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;

        MOB_CONSUME_SHIELD_DURABILITY = MOB_CONSUME_SHIELD_DURABILITY_CONFIG.get();
        MOB_ALWAYS_ANTICIPATE = MOB_ALWAYS_ANTICIPATE_CONFIG.get();
    }
}