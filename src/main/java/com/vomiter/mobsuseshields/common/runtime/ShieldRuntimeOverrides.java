package com.vomiter.mobsuseshields.common.runtime;

public final class ShieldRuntimeOverrides {
    private Integer useDuration;
    private Integer cooldownDuration;
    private Integer checkContinueToUseInterval;

    public Integer getUseDuration() {
        return useDuration;
    }

    public void setUseDuration(Integer useDuration) {
        this.useDuration = useDuration;
    }

    public Integer getCooldownDuration() {
        return cooldownDuration;
    }

    public void setCooldownDuration(Integer cooldownDuration) {
        this.cooldownDuration = cooldownDuration;
    }

    public Integer getCheckContinueToUseInterval() {
        return checkContinueToUseInterval;
    }

    public void setCheckContinueToUseInterval(Integer checkContinueToUseInterval) {
        this.checkContinueToUseInterval = checkContinueToUseInterval;
    }

    public boolean isEmpty() {
        return useDuration == null
                && cooldownDuration == null
                && checkContinueToUseInterval == null;
    }
}