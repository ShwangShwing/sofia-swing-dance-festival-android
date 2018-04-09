package com.sofiaswing.sofiaswingdancefestival.views.schedule;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;
import com.sofiaswing.sofiaswingdancefestival.models.EventModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
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
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;
    private ScheduleInterfaces.IView view;
    private CompositeDisposable subscriptions;

    public SchedulePresenter(DataInterfaces.IVenuesData venuesData, DataInterfaces.IEventsData eventsData, DataInterfaces.IClassLevelsData classLevelsData, ProvidersInterfaces.ISettingsProvider settingsProvider) {
        this.venuesData = venuesData;
        this.eventsData = eventsData;
        this.classLevelsData = classLevelsData;
        this.settingsProvider = settingsProvider;
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
                (List<VenueModel> venues, List<EventModel> events, List<ClassLevelModel> classLevels) -> {
                    List<ScheduleEventViewModel> eventViewModels = new ArrayList<>();
                    for (EventModel event : events) {
                        ScheduleEventViewModel newShdeculeEvent =
                                new ScheduleEventViewModel(
                                        event.getId(),
                                        event.getEventType(),
                                        event.getStartTime(),
                                        event.getEndTime(),
                                        event.getName(),
                                        event.getVenueId(),
                                        this.settingsProvider.isSubscribedForEvent(event.getId())
                                );
                        eventViewModels.add(newShdeculeEvent);
                    }

                    return new Object[]{venues, eventViewModels, classLevels};
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<Object[]>) venuesAndEvents -> {
                    Map<String, String> classLevelMap = new HashMap<>();
                    for (ClassLevelModel classLevel : (List<ClassLevelModel>)venuesAndEvents[2]) {
                        classLevelMap.put(classLevel.getId(), classLevel.getName());
                    }
                    this.view.setSchedule(
                            (List<VenueModel>)venuesAndEvents[0],
                            (List<ScheduleEventViewModel>)venuesAndEvents[1],
                            classLevelMap);
                }));

    }

    @Override
    public void stop() {
        this.subscriptions.clear();
    }
}
