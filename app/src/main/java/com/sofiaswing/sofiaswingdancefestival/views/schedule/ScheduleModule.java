package com.sofiaswing.sofiaswingdancefestival.views.schedule;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 3/18/18.
 */

@Module
public class ScheduleModule {
    @Provides
    ScheduleInterfaces.IPresenter provideSchedulePresenter(
            DataInterfaces.IVenuesData venuesData,
            DataInterfaces.IEventsData eventsData,
            DataInterfaces.IClassLevelsData classLevelsData,
            ProvidersInterfaces.ISettingsProvider settingsProvider) {
        return new SchedulePresenter(venuesData, eventsData, classLevelsData, settingsProvider);
    }
}
