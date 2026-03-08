package com.vomiter.mobsuseshields;

import com.mojang.logging.LogUtils;
import com.vomiter.mobsuseshields.common.event.EventHandler;
import com.vomiter.mobsuseshields.common.registry.ModRegistries;
import com.vomiter.mobsuseshields.data.ModDataGenerator;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MobsUseShields.MOD_ID)
public class MobsUseShields
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "mobsuseshields";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation modLoc(String path){
        return Helpers.id(MobsUseShields.MOD_ID, path);
    }

    public MobsUseShields(FMLJavaModLoadingContext context) {
        EventHandler.init();
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(ModDataGenerator::generateData);
        ModRegistries.register(modBus);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

}
