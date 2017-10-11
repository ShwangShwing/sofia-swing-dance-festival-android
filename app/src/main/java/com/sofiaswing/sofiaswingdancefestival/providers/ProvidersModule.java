package com.sofiaswing.sofiaswingdancefestival.providers;

import android.content.Context;
import android.location.LocationManager;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/9/17.
 */

@Module
public class ProvidersModule {
    private ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;
    private ProvidersInterfaces.ISettingsProvider settingsProvider;

    public ProvidersModule() {
        this.currentSsdfYearProvider = null;
        this.settingsProvider = null;
    }

    @Provides
    ProvidersInterfaces.IImageProvider provideImageProvider() {
        return new ImageProvider();
    }

    @Provides
    ProvidersInterfaces.ILocationProvider provideLocationProvider() {
        return new LocationProvider();
    }

    @Provides
    synchronized ProvidersInterfaces.ICurrentSsdfYearProvider provideCurrentSsdfYearProvider() {
        if (this.currentSsdfYearProvider == null) {
            this.currentSsdfYearProvider = new CurrentSsdfYearProvider();
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
}
