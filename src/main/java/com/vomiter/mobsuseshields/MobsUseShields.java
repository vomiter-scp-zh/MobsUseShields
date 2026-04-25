package com.vomiter.mobsuseshields;

import com.mojang.logging.LogUtils;
import com.vomiter.mobsuseshields.common.event.EventHandler;
import com.vomiter.mobsuseshields.data.MobShieldConfigReloadListener;
import com.vomiter.mobsuseshields.data.ModDataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
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

    public MobsUseShields(ModContainer mod, IEventBus modBus) {
        EventHandler.init();
        modBus.addListener(this::commonSetup);
        modBus.addListener(ModDataGenerator::generateData);
        mod.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        if(FMLEnvironment.dist.isClient()){
            modBus.addListener(this::clientSetup);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NeoForge.EVENT_BUS.addListener(MobShieldConfigReloadListener::onAddReloadListeners);
        });
    }

    private void clientSetup(final FMLClientSetupEvent event){
        //event.enqueueWork(EMFCompat::init);
    }
}
