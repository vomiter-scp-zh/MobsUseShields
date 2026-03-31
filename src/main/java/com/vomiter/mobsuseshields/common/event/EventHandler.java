package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.common.command.ModCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandler {
    public static boolean startSpawnCheck = false;

    public static void init(){
        final IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(EventHandler::onRegisterCommands);
        bus.addListener(EquipShieldEvent::onMobEquipShield);
        bus.addListener(MusMobSpawnEvent::onFinalizeSpawn);
        bus.addListener(EventHandler::onServerTick);
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommand.register(event.getDispatcher());
    }

    public static void onServerTick(TickEvent.ServerTickEvent event){
        if(startSpawnCheck) return;
        if(event.getServer().overworld().getGameTime() > 1200) startSpawnCheck = true;
    }

}
