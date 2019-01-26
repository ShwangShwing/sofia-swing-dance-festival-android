package com.sofiaswing.sofiaswingdancefestival.providers;

import java.util.Date;

public class CurrentTimeProvider implements ProvidersInterfaces.ICurrentTimeProvider {
    private final ProvidersInterfaces.IHackerSettingsProvider volatileSettingsProvider;

    public CurrentTimeProvider(ProvidersInterfaces.IHackerSettingsProvider volatileSettingsProvider) {
        this.volatileSettingsProvider = volatileSettingsProvider;
    }

    @Override
    public long getCurrentTimeMs() {
        long curRealTime = new Date().getTime();

        if (this.volatileSettingsProvider.isCurrentTimeOverriden()) {
            long overridenTime = this.volatileSettingsProvider.getOverridenTimeMs();
            if (this.volatileSettingsProvider.isCurrentOverridenTimeFrozen()) {
                return overridenTime;
            }

            long timeSinceOverride = curRealTime - this.volatileSettingsProvider.getTimeOverridenAtMs();
            return overridenTime + timeSinceOverride;
        }

        return curRealTime;
    }
}
