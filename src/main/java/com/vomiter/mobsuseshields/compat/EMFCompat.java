package com.vomiter.mobsuseshields.compat;

import com.vomiter.mobsuseshields.common.ICanUseShieldMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

public final class EMFCompat {
    private static boolean initialized = false;
    private static boolean available = false;

    private EMFCompat() {}

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        try {
            Class<?> contextClass = Class.forName(
                "traben.entity_model_features.models.animation.EMFAnimationEntityContext"
            );

            Field listenersField = contextClass.getField("forceVanillaModelListeners");
            Object raw = listenersField.get(null);
            if (!(raw instanceof List<?> rawList)) {
                return;
            }

            @SuppressWarnings("unchecked")
            List<Function<Object, Boolean>> listeners = (List<Function<Object, Boolean>>) rawList;

            listeners.add(emfEntity -> {
                try {
                    return shouldForceVanilla(emfEntity);
                } catch (Throwable ignored) {
                    return false;
                }
            });

            available = true;
        } catch (Throwable ignored) {
            // EMF 不存在、欄位名稱改掉、或 API 結構不同時，直接靜默略過
        }
    }

    public static boolean isAvailable() {
        return available;
    }

    private static boolean shouldForceVanilla(Object emfEntityObj) {
        if (!(emfEntityObj instanceof LivingEntity entity)) {
            return false;
        }

        if (!(entity instanceof Mob)) {
            return false;
        }

        if (!(entity instanceof ICanUseShieldMob)){
            return false;
        }

        if (!entity.isUsingItem()) {
            return false;
        }

        ItemStack using = entity.getUseItem();
        if (using.isEmpty()) {
            return false;
        }

        return using.getUseAnimation() == UseAnim.BLOCK;
    }
}