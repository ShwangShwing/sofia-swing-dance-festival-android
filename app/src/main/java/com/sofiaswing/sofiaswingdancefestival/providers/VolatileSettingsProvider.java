package com.sofiaswing.sofiaswingdancefestival.providers;


import java.util.Date;

/**
 * Created by shwangshwing on 2/1/18.
 */

public class VolatileSettingsProvider implements ProvidersInterfaces.IVolatileSettingsProvider {
    private final ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;

    private boolean isHackerEnabled = false;
    private boolean isSsdfYearFromDatabase = true;
    private boolean isCurrentTimeOverriden = false;
    private boolean isCurrentOverridenTimeFrozen = false;
    private long overridenTimeMs;
    private long timeOverridenAtMs;
    private String customSsdfYear = "";

    VolatileSettingsProvider(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        this.currentSsdfYearProvider = currentSsdfYearProvider;
    }

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
    public boolean isYearFromDatabase() {
        return isSsdfYearFromDatabase;
    }

    @Override
    public String getCurrentCustomSsdfYear() {
        return isSsdfYearFromDatabase ? "" : customSsdfYear;
    }

    @Override
    public void setCurrentSsdfYearFromData() {
        this.isSsdfYearFromDatabase = true;
        this.currentSsdfYearProvider.setCurrentSsdfYearFromData();
    }

    @Override
    public void setCurrentSsdfYear(String currentSsdfYear) {
        this.isSsdfYearFromDatabase = false;
        this.customSsdfYear = currentSsdfYear;
        this.currentSsdfYearProvider.setCurrentSsdfYear(currentSsdfYear);
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
