package com.sofiaswing.sofiaswingdancefestival.views.schedule;

import android.util.Pair;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;
import com.sofiaswing.sofiaswingdancefestival.models.EventModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 3/18/18.
 */

public class SchedulePresenter implements ScheduleInterfaces.IPresenter {
    private final DataInterfaces.IVenuesData venuesData;
    private final DataInterfaces.IEventsData eventsData;
    private final DataInterfaces.IClassLevelsData classLevelsData;
    private ScheduleInterfaces.IView view;
    private CompositeDisposable subscriptions;

    public SchedulePresenter(DataInterfaces.IVenuesData venuesData, DataInterfaces.IEventsData eventsData, DataInterfaces.IClassLevelsData classLevelsData) {
        this.venuesData = venuesData;
        this.eventsData = eventsData;
        this.classLevelsData = classLevelsData;
        this.subscriptions = new CompositeDisposable();
    }

    public void setView(ScheduleInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        Observable venuesObs = this.venuesData.getAll();
        Observable eventsObs = this.eventsData.getEvents(null);
        Observable classLevelsObs = this.classLevelsData.getAll();
        subscriptions.add(Observable.combineLatest(venuesObs, eventsObs, classLevelsObs,
                (List<VenueModel> venues, List<EventModel> events, List<ClassLevelModel> classLevels) ->
                        new Object[]{venues, events, classLevels})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<Object[]>) venuesAndEvents -> {
                    Map<String, String> classLevelMap = new HashMap<>();
                    for (ClassLevelModel classLevel : (List<ClassLevelModel>)venuesAndEvents[2]) {
                        classLevelMap.put(classLevel.getId(), classLevel.getName());
                    }
                    this.view.setSchedule(
                            (List<VenueModel>)venuesAndEvents[0],
                            (List<EventModel>)venuesAndEvents[1],
                            classLevelMap);
                }));

    }

    @Override
    public void stop() {
        this.subscriptions.clear();
    }
}
