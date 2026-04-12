package com.vomiter.mobsuseshields.api;

import com.vomiter.mobsuseshields.common.runtime.MobShieldRuntimeAccess;
import com.vomiter.mobsuseshields.common.runtime.MobShieldValueResolver;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

/**
 * Stable public API for interacting with Mobs Use Shields runtime shield behavior.
 *
 * <p>This class is intended for external mods and integrations.</p>
 *
 * <p>Compatibility contract:
 * <ul>
 *     <li>The methods in this class are intended to remain source-compatible across normal updates.</li>
 *     <li>Callers should depend on this class rather than internal runtime/storage classes.</li>
 *     <li>Internal storage details (persistent NBT, attachments, capabilities, etc.) are NOT part of the API contract.</li>
 * </ul>
 * </p>
 *
 * <p>Semantics:
 * <ul>
 *     <li>"Override" means per-entity runtime value.</li>
 *     <li>"Effective" means override if present, otherwise fallback to entity-type default config.</li>
 *     <li>Passing {@code null} to a setter clears that specific override.</li>
 * </ul>
 * </p>
 */
public final class MobShieldApi {
    private MobShieldApi() {
    }

    public static @Nullable Integer getUseDurationOverride(Mob mob) {
        return MobShieldRuntimeAccess.getOverrides(mob).getUseDuration();
    }

    public static @Nullable Integer getCooldownDurationOverride(Mob mob) {
        return MobShieldRuntimeAccess.getOverrides(mob).getCooldownDuration();
    }

    public static @Nullable Integer getCheckContinueToUseIntervalOverride(Mob mob) {
        return MobShieldRuntimeAccess.getOverrides(mob).getCheckContinueToUseInterval();
    }

    public static int getEffectiveUseDuration(Mob mob) {
        return MobShieldValueResolver.getUseDuration(mob);
    }

    public static int getEffectiveCooldownDuration(Mob mob) {
        return MobShieldValueResolver.getCooldownDuration(mob);
    }

    public static int getEffectiveCheckContinueToUseInterval(Mob mob) {
        return MobShieldValueResolver.getCheckContinueToUseInterval(mob);
    }

    public static void setUseDurationOverride(Mob mob, @Nullable Integer value) {
        validateNullablePositive("useDuration", value);
        MobShieldRuntimeAccess.setUseDuration(mob, value);
    }

    public static void setCooldownDurationOverride(Mob mob, @Nullable Integer value) {
        validateNullableNonNegative("cooldownDuration", value);
        MobShieldRuntimeAccess.setCooldownDuration(mob, value);
    }

    public static void setCheckContinueToUseIntervalOverride(Mob mob, @Nullable Integer value) {
        validateNullablePositive("checkContinueToUseInterval", value);
        MobShieldRuntimeAccess.setCheckContinueToUseInterval(mob, value);
    }

    public static void clearUseDurationOverride(Mob mob) {
        MobShieldRuntimeAccess.setUseDuration(mob, null);
    }

    public static void clearCooldownDurationOverride(Mob mob) {
        MobShieldRuntimeAccess.setCooldownDuration(mob, null);
    }

    public static void clearCheckContinueToUseIntervalOverride(Mob mob) {
        MobShieldRuntimeAccess.setCheckContinueToUseInterval(mob, null);
    }

    public static void clearAllOverrides(Mob mob) {
        MobShieldRuntimeAccess.clearOverrides(mob);
    }

    public static boolean hasAnyOverrides(Mob mob) {
        return !MobShieldRuntimeAccess.getOverrides(mob).isEmpty();
    }

    private static void validateNullablePositive(String name, @Nullable Integer value) {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException(name + " must be > 0, got " + value);
        }
    }

    private static void validateNullableNonNegative(String name, @Nullable Integer value) {
        if (value != null && value < 0) {
            throw new IllegalArgumentException(name + " must be >= 0, got " + value);
        }
    }
}