package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.common.command.ModCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandler {
    public static void init(){
        final IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(EventHandler::onRegisterCommands);
        bus.addListener(EquipShieldEvent::onMobEquipShield);
        bus.addListener(MusMobSpawnEvent::onFinalizeSpawn);
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommand.register(event.getDispatcher());
    }


}
