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
    private ProvidersInterfaces.IHackerSettingsProvider volatileSettingsProvider;

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
    synchronized ProvidersInterfaces.ISettingsProvider provideSettingsProvider(
            Context context,
            ProvidersInterfaces.ISerializer serializer,
            ProvidersInterfaces.IEventAlarmManager eventAlarmManager) {
        if (this.settingsProvider == null) {
            this.settingsProvider = new SharedPreferencesSettingsProvider(context, serializer, eventAlarmManager);
        }

        return this.settingsProvider;
    }

    @Provides
    synchronized ProvidersInterfaces.IHackerSettingsProvider provideVolatileSettingsProvider(ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider) {
        if (this.volatileSettingsProvider == null) {
            this.volatileSettingsProvider = new HackerSettingsProvider(currentSsdfYearProvider);
        }

        return this.volatileSettingsProvider;
    }

    @Provides
    ProvidersInterfaces.ICurrentTimeProvider provideCurrentTimeProvider(ProvidersInterfaces.IHackerSettingsProvider volatileSettingsProvider) {
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

    @Provides
    ProvidersInterfaces.ISerializer provideSerializer() {
        return new GsonSerializer();
    }

    @Provides
    ProvidersInterfaces.IEventAlarmManager provideEventAlarmManager(
            Context context,
            ProvidersInterfaces.ICurrentTimeProvider timeProvider) {
        return new EventAlarmManager(context, timeProvider);
    }

    @Provides
    ProvidersInterfaces.INetworkImageLoader provideNetworkImageLoader(Context ctx, ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider) {
        return new CachingNetworkImageLoader(ctx, currentTimeProvider);
    }
}
