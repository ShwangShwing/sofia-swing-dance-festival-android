package com.sofiaswing.sofiaswingdancefestival.providers;


/**
 * Created by shwangshwing on 2/1/18.
 */

public class VolatileSettingsProvider implements ProvidersInterfaces.IVolatileSettingsProvider {
    private boolean isHackerEnabled = false;

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
}
