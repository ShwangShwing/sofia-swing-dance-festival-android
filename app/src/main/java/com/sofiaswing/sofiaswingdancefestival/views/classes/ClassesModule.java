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
    ClassesInterfaces.IPresenter provideClassesPresenter(DataInterfaces.IClassLevelsData classLevelsFirebaseData) {
        return new ClassesPresenter(classLevelsFirebaseData);
    }

    @Provides
    ClassesInterfaces.IClassesLevelPresenter provideClassesLevelPresenter(
            DataInterfaces.IEventsData eventsData,
            DataInterfaces.IVenuesData venuesData,
            DataInterfaces.IInstructorsData instructorsData,
            ProvidersInterfaces.ISettingsProvider settingsProvider) {
        return new ClassesLevelPresenter(venuesData, eventsData, instructorsData, settingsProvider);
    }
}
