package com.vomiter.mobsuseshields.common.runtime;

import com.vomiter.mobsuseshields.data.MobShieldConfig;
import com.vomiter.mobsuseshields.data.MobShieldConfigManager;
import net.minecraft.world.entity.Mob;

public final class MobShieldValueResolver {
    private MobShieldValueResolver() {
    }

    public static int getUseDuration(Mob mob) {
        ShieldRuntimeOverrides overrides = MobShieldRuntimeAccess.getOverrides(mob);
        if (overrides.getUseDuration() != null) {
            return overrides.getUseDuration();
        }

        MobShieldConfig config = MobShieldConfigManager.get(mob.getType());
        return config.useDuration();
    }

    public static int getCooldownDuration(Mob mob) {
        ShieldRuntimeOverrides overrides = MobShieldRuntimeAccess.getOverrides(mob);
        if (overrides.getCooldownDuration() != null) {
            return overrides.getCooldownDuration();
        }

        MobShieldConfig config = MobShieldConfigManager.get(mob.getType());
        return config.cooldownDuration();
    }

    public static int getCheckContinueToUseInterval(Mob mob) {
        ShieldRuntimeOverrides overrides = MobShieldRuntimeAccess.getOverrides(mob);
        if (overrides.getCheckContinueToUseInterval() != null) {
            return overrides.getCheckContinueToUseInterval();
        }

        MobShieldConfig config = MobShieldConfigManager.get(mob.getType());
        return config.checkContinueToUseInterval();
    }
}