package com.sofiaswing.sofiaswingdancefestival.providers;

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
    Lock currentSsdfYearProviderLock;
    private ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;

    public ProvidersModule() {
        this.currentSsdfYearProviderLock = new ReentrantLock();
        this.currentSsdfYearProvider = null;
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
    ProvidersInterfaces.ICurrentSsdfYearProvider provideCurrentSsdfYearProvider() {
        currentSsdfYearProviderLock.lock();
        if (this.currentSsdfYearProvider == null) {
            this.currentSsdfYearProvider = new CurrentSsdfYearProvider();
        }

        currentSsdfYearProviderLock.unlock();
        return this.currentSsdfYearProvider;
    }
}
