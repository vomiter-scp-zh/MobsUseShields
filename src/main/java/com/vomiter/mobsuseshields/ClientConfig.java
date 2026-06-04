package com.vomiter.mobsuseshields;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = MobsUseShields.MOD_ID)
public class ClientConfig {
    public static boolean HIDE_PILLAGER_SHIELD_IN_ARMS = true;

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue HIDE_PILLAGER_SHIELD_IN_ARMS_CONFIG;

    static {
        BUILDER.push("client");

        HIDE_PILLAGER_SHIELD_IN_ARMS_CONFIG = BUILDER
                .comment("If false, illagers with shield do not cross their hand.")
                .define("hidePillagerShieldInArms", true);

        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;

        HIDE_PILLAGER_SHIELD_IN_ARMS = HIDE_PILLAGER_SHIELD_IN_ARMS_CONFIG.get();
    }
}