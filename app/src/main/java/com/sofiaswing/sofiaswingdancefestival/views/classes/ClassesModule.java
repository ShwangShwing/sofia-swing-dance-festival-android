package com.sofiaswing.sofiaswingdancefestival.views.classes;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/10/17.
 */

@Module
public class ClassesModule {
    @Provides
    ClassesInterfaces.IView provideNewsView() {
        return new ClassesView();
    }

    @Provides
    ClassesInterfaces.IPresenter provideNewsPresenter(
            ClassesInterfaces.IView view, DataInterfaces.IClassLevelsData classLevelsFirebaseData, DataInterfaces.IEventsData eventsData) {
        return new ClassesPresenter(view, classLevelsFirebaseData, eventsData);
    }
}
