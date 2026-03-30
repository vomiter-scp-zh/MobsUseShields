package com.vomiter.mobsuseshields;

import com.mojang.logging.LogUtils;
import com.vomiter.mobsuseshields.common.event.EventHandler;
import com.vomiter.mobsuseshields.common.registry.ModRegistries;
import com.vomiter.mobsuseshields.data.MobShieldConfigReloadListener;
import com.vomiter.mobsuseshields.data.ModDataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
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
        if(FMLEnvironment.dist.isClient()){
            modBus.addListener(this::clientSetup);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            MinecraftForge.EVENT_BUS.addListener(MobShieldConfigReloadListener::onAddReloadListeners);
        });
    }

    private void clientSetup(final FMLClientSetupEvent event){
        //event.enqueueWork(EMFCompat::init);
    }
}
