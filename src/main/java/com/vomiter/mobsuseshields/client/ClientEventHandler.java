package com.vomiter.mobsuseshields.client;

import com.google.common.reflect.TypeToken;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import org.jetbrains.annotations.NotNull;

public class ClientEventHandler {
    public static final ContextKey<@NotNull Boolean> OFFHAND_SHIELD =
            new ContextKey<>(Identifier.fromNamespaceAndPath("mobsuseshields", "offhand_shield"));

    public static final ContextKey<@NotNull Boolean> USING_SHIELD =
            new ContextKey<>(Identifier.fromNamespaceAndPath("mobsuseshields", "using_shield"));


    public static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(
                new TypeToken<@NotNull LivingEntityRenderer<@NotNull LivingEntity, @NotNull LivingEntityRenderState, ?>>() {},
                (entity, state)->{
                    if (!(entity instanceof Mob mob)) {
                        return;
                    }

                    boolean offhandShield =
                            mob.getOffhandItem().getItem() instanceof ShieldItem;

                    ItemStack using = mob.getUseItem();

                    boolean usingShield =
                            mob.isUsingItem()
                                    && !using.isEmpty()
                                    && using.getItem() instanceof ShieldItem;

                    state.setRenderData(OFFHAND_SHIELD, offhandShield);
                    state.setRenderData(USING_SHIELD, usingShield);
                }
        );
    }

}
