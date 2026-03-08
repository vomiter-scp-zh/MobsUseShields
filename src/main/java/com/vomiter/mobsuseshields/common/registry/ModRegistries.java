package com.vomiter.mobsuseshields.common.registry;

import com.vomiter.mobsuseshields.MobsUseShields;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModRegistries {
    static List<DeferredRegister<?>> REGISTRIES = new ArrayList<>();
    public static <B> DeferredRegister<B> createRegistry(IForgeRegistry<B> b){
        return DeferredRegister.create(b, MobsUseShields.MOD_ID);
    }
    public static <B> DeferredRegister<B> createRegistry(ResourceKey b){
        return DeferredRegister.create(b, MobsUseShields.MOD_ID);
    }

    static void add(DeferredRegister<?> r){
        REGISTRIES.add(r);
    }

    public static void register(IEventBus modBus){
        add(ModItems.ITEMS);
        add(ModBlocks.BLOCKS);
        for (DeferredRegister<?> registry : REGISTRIES) {
            registry.register(modBus);
        }
    }
}
