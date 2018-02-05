package com.sofiaswing.sofiaswingdancefestival.providers;


/**
 * Created by shwangshwing on 2/1/18.
 */

public class VolatileSettingsProvider implements ProvidersInterfaces.IVolatileSettingsProvider {
    private final ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;

    private boolean isHackerEnabled = false;
    private boolean isSsdfYearFromDatabase = true;
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
}
