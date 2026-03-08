package com.vomiter.mobsuseshields;

import net.minecraft.resources.ResourceLocation;

public class Helpers {
    public static ResourceLocation id(String namespace, String path){
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation id(String path){
        return id(MobsUseShields.MOD_ID, path);
    }
}
