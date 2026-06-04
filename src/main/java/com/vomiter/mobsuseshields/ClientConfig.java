package com.vomiter.mobsuseshields;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = MobsUseShields.MOD_ID)
public class ClientConfig {
    public static boolean HIDE_PILLAGER_SHIELD_IN_ARMS = true;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue HIDE_PILLAGER_SHIELD_IN_ARMS_CONFIG;

    static {
        BUILDER.push("client");

        HIDE_PILLAGER_SHIELD_IN_ARMS_CONFIG = BUILDER
                .comment("If false, illagers with shield do not cross their hand.")
                .define("hidePillagerShieldInArms", true);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;

        HIDE_PILLAGER_SHIELD_IN_ARMS = HIDE_PILLAGER_SHIELD_IN_ARMS_CONFIG.get();
    }
}