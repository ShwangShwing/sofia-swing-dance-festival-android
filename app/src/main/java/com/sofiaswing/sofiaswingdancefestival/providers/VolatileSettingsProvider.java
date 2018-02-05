package com.sofiaswing.sofiaswingdancefestival.providers;


/**
 * Created by shwangshwing on 2/1/18.
 */

public class VolatileSettingsProvider implements ProvidersInterfaces.IVolatileSettingsProvider {
    private final ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;

    private boolean isHackerEnabled = false;

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
    public void setCurrentSsdfYearFromData() {
        this.currentSsdfYearProvider.setCurrentSsdfYearFromData();
    }

    @Override
    public void setCurrentSsdfYear(String currentSsdfYear) {
        this.currentSsdfYearProvider.setCurrentSsdfYear(currentSsdfYear);
    }
}
