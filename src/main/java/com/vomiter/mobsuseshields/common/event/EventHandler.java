package com.vomiter.mobsuseshields.common.event;

import com.vomiter.mobsuseshields.common.command.ModCommand;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class EventHandler {

    public static void init(){
        final IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(EventHandler::onRegisterCommands);
        bus.addListener(EquipShieldEvent::onMobEquipShield);
        bus.addListener(MusMobSpawnEvent::onFinalizeSpawn);
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommand.register(event.getDispatcher());
    }

}
