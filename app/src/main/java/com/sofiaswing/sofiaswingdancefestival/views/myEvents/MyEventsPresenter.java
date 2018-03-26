package com.sofiaswing.sofiaswingdancefestival.views.myEvents;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;
import com.sofiaswing.sofiaswingdancefestival.models.EventModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 3/5/18.
 */

public class MyEventsPresenter implements MyEventsInterfaces.IPresenter {
    private MyEventsInterfaces.IView view;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;
    private final DataInterfaces.IEventsData eventsData;
    private final DataInterfaces.IVenuesData venuesData;
    private final DataInterfaces.IClassLevelsData classLevelsData;

    private final List<EventViewModel> events;

    private final CompositeDisposable eventSubscriptions;
    private final CompositeDisposable classLevelsSubscriptions;
    private final CompositeDisposable venuesSubscriptions;

    public MyEventsPresenter(
            ProvidersInterfaces.ISettingsProvider settingsProvider,
            DataInterfaces.IEventsData eventsData,
            DataInterfaces.IVenuesData venuesData,
            DataInterfaces.IClassLevelsData classLevelsData) {
        this.settingsProvider = settingsProvider;
        this.eventsData = eventsData;
        this.venuesData = venuesData;
        this.classLevelsData = classLevelsData;

        this.events = new ArrayList<>();

        this.eventSubscriptions = new CompositeDisposable();
        this.classLevelsSubscriptions = new CompositeDisposable();
        this.venuesSubscriptions = new CompositeDisposable();
    }

    @Override
    public void setView(MyEventsInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        this.classLevelsSubscriptions.add(this.classLevelsData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(classLevels -> {
                    for (ClassLevelModel classLevel : classLevels) {
                        view.setClassLevelString(classLevel.getId(), classLevel.getName());
                    }
                }));


        List<String> eventIds = this.settingsProvider.getSubscribedEventsIds();

        this.eventSubscriptions.add(this.eventsData.getEventsByIds(eventIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventModels -> {
                    events.clear();
                    venuesSubscriptions.clear();
                    for (EventModel event : eventModels) {
                        final EventViewModel viewEvent = new EventViewModel(
                                event.getId(),
                                event.getEventType(),
                                event.getStartTime(),
                                event.getEndTime(),
                                event.getName(),
                                null,
                                settingsProvider.isSubscribedForEvent(event.getId()));

                        events.add(viewEvent);
                        final int eventPosition = events.size() - 1;

                        venuesSubscriptions.add(this.venuesData.getById(event.getVenueId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<VenueModel>() {
                                    @Override
                                    public void accept(VenueModel venueModel) throws Exception {
                                        view.setEventVenue(eventPosition, venueModel);
                                    }
                                }));
                    }

                    this.view.setEvents(events);
                }));
    }

    @Override
    public void stop() {
        this.eventSubscriptions.clear();
        classLevelsSubscriptions.clear();
        this.venuesSubscriptions.clear();
    }

    @Override
    public void setEventSubscription(int position, boolean subscriptionStatus) {
        EventViewModel event = this.events.get(position);
        if (subscriptionStatus) {
            this.settingsProvider.subscribeForEvent(
                    event.getId(),
                    event.getName(),
                    event.getStartTime().getTime() / 1000, 0);
        }
        else {
            this.settingsProvider.unsubscribeFromEvent(event.getId());
        }

        this.view.setEventSubscriptionState(position, subscriptionStatus);
    }
}
