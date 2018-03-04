package com.sofiaswing.sofiaswingdancefestival.views.classes;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/10/17.
 */

@Module
public class ClassesModule {

    @Provides
    ClassesInterfaces.IPresenter provideNewsPresenter(
            DataInterfaces.IClassLevelsData classLevelsFirebaseData,
            DataInterfaces.IEventsData eventsData,
            DataInterfaces.IVenuesData venuesData,
            DataInterfaces.IInstructorsData instructorsData,
            ProvidersInterfaces.ISettingsProvider settingsProvider) {
        return new ClassesPresenter(classLevelsFirebaseData, venuesData, eventsData, instructorsData, settingsProvider);
    }
}
