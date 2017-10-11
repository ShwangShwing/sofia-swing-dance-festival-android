package com.sofiaswing.sofiaswingdancefestival.views.classes;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class ClassesPresenter implements ClassesInterfaces.IPresenter {
    private final ClassesInterfaces.IView view;
    private final DataInterfaces.IClassLevelsData classLevelsFirebaseData;
    private final DataInterfaces.IEventsData eventsData;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;

    public ClassesPresenter(ClassesInterfaces.IView view,
                            DataInterfaces.IClassLevelsData classLevelsFirebaseData,
                            DataInterfaces.IEventsData eventsData,
                            ProvidersInterfaces.ISettingsProvider settingsProvider) {
        this.view = view;
        this.eventsData = eventsData;
        this.settingsProvider = settingsProvider;
        this.view.setPresenter(this);
        this.classLevelsFirebaseData = classLevelsFirebaseData;
    }

    @Override
    public ClassesInterfaces.IView getView() {
        return view;
    }

    @Override
    public void start() {
        this.classLevelsFirebaseData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ClassLevelModel>>() {
                    @Override
                    public void accept(List<ClassLevelModel> classLevels) throws Exception {
                        view.setClassesTabs(classLevels);
                    }
                });
    }

    @Override
    public Observable<List<ClassModel>> getClassesByLevel(String type) {
        return this.eventsData.getClassesByLevel(type);
    }

    @Override
    public Observable<List<ClassModel>> getTasterClasses() {
        return this.eventsData.getTasterClasses();
    }

    @Override
    public void subscribeForEvent(String eventId, String eventName, int startTimestamp) {
        this.settingsProvider.subscribeForEvent(eventId, eventName, startTimestamp, 0);
    }

    @Override
    public void unsubscribeFromEvent(String eventId) {
        this.settingsProvider.unsubscribeFromEvent(eventId);
    }

    @Override
    public boolean isSubscribedForEvent(String eventId) {
        return this.settingsProvider.isSubscribedForEvent(eventId);
    }
}
