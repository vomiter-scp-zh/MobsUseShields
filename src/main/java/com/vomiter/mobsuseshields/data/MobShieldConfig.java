package com.vomiter.mobsuseshields.data;

public record MobShieldConfig(
        int useDuration,
        int cooldownDuration,
        int checkContinueToUseInterval
) {
    public static final MobShieldConfig DEFAULT = new MobShieldConfig(60, 60, 30);

    public MobShieldConfig {
        if (useDuration < 1) {
            throw new IllegalArgumentException("useDuration must be >= 1");
        }
        if (cooldownDuration < 0) {
            throw new IllegalArgumentException("cooldownDuration must be >= 0");
        }
        if (checkContinueToUseInterval < 1) {
            throw new IllegalArgumentException("checkContinueToUseInterval must be >= 1");
        }
    }
}