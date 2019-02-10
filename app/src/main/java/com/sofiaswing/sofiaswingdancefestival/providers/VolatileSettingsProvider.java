package com.sofiaswing.sofiaswingdancefestival.providers;


import java.util.Date;

/**
 * Created by shwangshwing on 2/1/18.
 */

public class VolatileSettingsProvider implements ProvidersInterfaces.IVolatileSettingsProvider {
    private boolean isHackerEnabled = false;
    private boolean isCurrentTimeOverriden = false;
    private boolean isCurrentOverridenTimeFrozen = false;
    private long overridenTimeMs;
    private long timeOverridenAtMs;

    @Override
    public boolean isHackerModeEnabled() {
        return isHackerEnabled;
    }

    @Override
    public void enableHackerMode() {
        this.isHackerEnabled = true;
    }

    @Override
    public void disableHackerMode() {
        this.isHackerEnabled = false;
    }

    @Override
    public void setOverrideCurrentTime(boolean override, boolean freezeTime, long overridenTime) {
        this.isCurrentTimeOverriden = override;
        this.isCurrentOverridenTimeFrozen = freezeTime;
        this.overridenTimeMs = overridenTime;
        this.timeOverridenAtMs = new Date().getTime();
    }

    @Override
    public boolean isCurrentTimeOverriden() {
        return this.isCurrentTimeOverriden;
    }

    @Override
    public boolean isCurrentOverridenTimeFrozen() {
        return this.isCurrentOverridenTimeFrozen;
    }

    @Override
    public long getOverridenTimeMs() {
        return this.overridenTimeMs;
    }

    @Override
    public long getTimeOverridenAtMs() {
        return this.timeOverridenAtMs;
    }
}
