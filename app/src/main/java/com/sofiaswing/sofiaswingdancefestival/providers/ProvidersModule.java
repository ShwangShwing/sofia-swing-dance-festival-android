package com.sofiaswing.sofiaswingdancefestival.providers;

import android.content.Context;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/9/17.
 */

@Module
public class ProvidersModule {
    private ProvidersInterfaces.ILocationProvider locationProvider;
    private ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;
    private ProvidersInterfaces.ISettingsProvider settingsProvider;
    private ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider;

    public ProvidersModule() {
        this.currentSsdfYearProvider = null;
        this.settingsProvider = null;
    }

    @Provides
    ProvidersInterfaces.IImageProvider provideImageProvider() {
        return new ImageProvider();
    }

    @Provides
    synchronized ProvidersInterfaces.ILocationProvider provideLocationProvider() {
        if (this.locationProvider == null) {
            this.locationProvider = new LocationProvider();
        }

        return this.locationProvider;
    }

    @Provides
    synchronized ProvidersInterfaces.ICurrentSsdfYearProvider provideCurrentSsdfYearProvider(
            DataInterfaces.ICurrentSsdfYearData currentSsdfYearData
    ) {
        if (this.currentSsdfYearProvider == null) {
            this.currentSsdfYearProvider = new CurrentSsdfYearProvider(currentSsdfYearData);
        }

        return this.currentSsdfYearProvider;
    }

    @Provides
    synchronized ProvidersInterfaces.ISettingsProvider provideSettingsProvider(Context context) {
        if (this.settingsProvider == null) {
            this.settingsProvider = new SettingsProvider(context);
        }

        return this.settingsProvider;
    }

    @Provides
    synchronized ProvidersInterfaces.IVolatileSettingsProvider provideVolatileSettingsProvider() {
        if (this.volatileSettingsProvider == null) {
            this.volatileSettingsProvider = new VolatileSettingsProvider();
        }

        return this.volatileSettingsProvider;
    }
}
