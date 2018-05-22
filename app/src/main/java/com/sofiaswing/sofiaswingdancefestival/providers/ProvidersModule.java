package com.sofiaswing.sofiaswingdancefestival.providers;

import android.content.Context;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.Firebase.FirebaseCloudMessagingProvider;

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

    @Provides
    synchronized ProvidersInterfaces.ILocationProvider provideLocationProvider(Context context) {
        if (this.locationProvider == null) {
            this.locationProvider = new LocationProvider(context);
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
    synchronized ProvidersInterfaces.IVolatileSettingsProvider provideVolatileSettingsProvider(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        if (this.volatileSettingsProvider == null) {
            this.volatileSettingsProvider = new VolatileSettingsProvider(currentSsdfYearProvider);
        }

        return this.volatileSettingsProvider;
    }

    @Provides
    ProvidersInterfaces.ICurrentTimeProvider provideCurrentTimeProvider(ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider) {
        return new CurrentTimeProvider(volatileSettingsProvider);
    }

    @Provides
    ProvidersInterfaces.IPushNotificationsProvider providePushNotificationProvider() {
        return new FirebaseCloudMessagingProvider();
    }

    @Provides
    ProvidersInterfaces.IEventSubscriptionRefresher provideEventSubscriptionRefresher(
            DataInterfaces.IEventsData eventsData,
            ProvidersInterfaces.ISettingsProvider settingsProvider
    ) {
        return new EventSubscriptionRefresher(eventsData, settingsProvider);
    }
}
