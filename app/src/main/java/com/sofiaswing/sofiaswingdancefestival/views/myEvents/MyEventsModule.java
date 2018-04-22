package com.sofiaswing.sofiaswingdancefestival.views.myEvents;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 3/5/18.
 */

@Module
public class MyEventsModule {
    @Provides
    MyEventsInterfaces.IPresenter provideMyEventsPresenter(
            ProvidersInterfaces.ISettingsProvider settingsProvider,
            DataInterfaces.IEventsData eventsData,
            DataInterfaces.IVenuesData venuesData,
            DataInterfaces.IClassLevelsData classLevelsData,
            ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider) {
        return new MyEventsPresenter(settingsProvider,
                eventsData,
                venuesData,
                classLevelsData,
                currentTimeProvider);
    }
}
