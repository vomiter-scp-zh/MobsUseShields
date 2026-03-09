package com.vomiter.mobsuseshields;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = MobsUseShields.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static boolean MOB_CONSUME_SHIELD_DURABILITY = false;
    public static boolean MOB_ALWAYS_ANTICIPATE = false;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue MOB_CONSUME_SHIELD_DURABILITY_CONFIG;
    private static final ForgeConfigSpec.BooleanValue MOB_ALWAYS_ANTICIPATE_CONFIG;

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

    static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;

        MOB_CONSUME_SHIELD_DURABILITY = MOB_CONSUME_SHIELD_DURABILITY_CONFIG.get();
        MOB_ALWAYS_ANTICIPATE = MOB_ALWAYS_ANTICIPATE_CONFIG.get();
    }
}