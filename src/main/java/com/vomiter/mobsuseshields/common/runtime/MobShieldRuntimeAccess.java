package com.vomiter.mobsuseshields.common.runtime;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Mob;

public final class MobShieldRuntimeAccess {
    private static final String ROOT = "mobsuseshields";
    private static final String OVERRIDES = "runtime_overrides";
    private static final String USE_DURATION = "use_duration";
    private static final String COOLDOWN_DURATION = "cooldown_duration";
    private static final String CHECK_CONTINUE_TO_USE_INTERVAL = "check_continue_to_use_interval";

    private MobShieldRuntimeAccess() {
    }

    public static ShieldRuntimeOverrides getOverrides(Mob mob) {
        CompoundTag persistent = mob.getPersistentData();
        ShieldRuntimeOverrides overrides = new ShieldRuntimeOverrides();

        if (!persistent.contains(ROOT, Tag.TAG_COMPOUND)) {
            return overrides;
        }

        CompoundTag root = persistent.getCompound(ROOT);
        if (!root.contains(OVERRIDES, Tag.TAG_COMPOUND)) {
            return overrides;
        }

        CompoundTag tag = root.getCompound(OVERRIDES);

        if (tag.contains(USE_DURATION, Tag.TAG_INT)) {
            overrides.setUseDuration(tag.getInt(USE_DURATION));
        }
        if (tag.contains(COOLDOWN_DURATION, Tag.TAG_INT)) {
            overrides.setCooldownDuration(tag.getInt(COOLDOWN_DURATION));
        }
        if (tag.contains(CHECK_CONTINUE_TO_USE_INTERVAL, Tag.TAG_INT)) {
            overrides.setCheckContinueToUseInterval(tag.getInt(CHECK_CONTINUE_TO_USE_INTERVAL));
        }

        return overrides;
    }

    public static void setOverrides(Mob mob, ShieldRuntimeOverrides overrides) {
        CompoundTag persistent = mob.getPersistentData();

        if (overrides == null || overrides.isEmpty()) {
            clearOverrides(mob);
            return;
        }

        CompoundTag root = persistent.contains(ROOT, Tag.TAG_COMPOUND)
                ? persistent.getCompound(ROOT)
                : new CompoundTag();

        CompoundTag tag = new CompoundTag();

        if (overrides.getUseDuration() != null) {
            tag.putInt(USE_DURATION, overrides.getUseDuration());
        }
        if (overrides.getCooldownDuration() != null) {
            tag.putInt(COOLDOWN_DURATION, overrides.getCooldownDuration());
        }
        if (overrides.getCheckContinueToUseInterval() != null) {
            tag.putInt(CHECK_CONTINUE_TO_USE_INTERVAL, overrides.getCheckContinueToUseInterval());
        }

        root.put(OVERRIDES, tag);
        persistent.put(ROOT, root);
    }

    public static void clearOverrides(Mob mob) {
        CompoundTag persistent = mob.getPersistentData();
        if (!persistent.contains(ROOT, Tag.TAG_COMPOUND)) {
            return;
        }

        CompoundTag root = persistent.getCompound(ROOT);
        root.remove(OVERRIDES);

        if (root.isEmpty()) {
            persistent.remove(ROOT);
        }
        else {
            persistent.put(ROOT, root);
        }
    }

    public static void setUseDuration(Mob mob, Integer value) {
        ShieldRuntimeOverrides overrides = getOverrides(mob);
        overrides.setUseDuration(value);
        setOverrides(mob, overrides);
    }

    public static void setCooldownDuration(Mob mob, Integer value) {
        ShieldRuntimeOverrides overrides = getOverrides(mob);
        overrides.setCooldownDuration(value);
        setOverrides(mob, overrides);
    }

    public static void setCheckContinueToUseInterval(Mob mob, Integer value) {
        ShieldRuntimeOverrides overrides = getOverrides(mob);
        overrides.setCheckContinueToUseInterval(value);
        setOverrides(mob, overrides);
    }
}