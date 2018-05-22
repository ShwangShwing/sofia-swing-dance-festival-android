package com.sofiaswing.sofiaswingdancefestival.providers;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.EventModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class EventSubscriptionRefresher implements ProvidersInterfaces.IEventSubscriptionRefresher {
    private final DataInterfaces.IEventsData eventsData;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;
    private final CompositeDisposable subscriptions;

    public EventSubscriptionRefresher(DataInterfaces.IEventsData eventsData, ProvidersInterfaces.ISettingsProvider settingsProvider) {
        this.eventsData = eventsData;
        this.settingsProvider = settingsProvider;
        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public void refreshEventSubscriptions() {
        this.subscriptions.clear();
        List<String> subscribedEventIds = this.settingsProvider.getSubscribedEventsIds();
        this.subscriptions.add(
                this.eventsData.getEvents(subscribedEventIds)
                .subscribe(eventModels -> {
                    for (EventModel eventModel : eventModels) {
                        this.settingsProvider.updateEventSubscription(
                                eventModel.getId(),
                                eventModel.getName(),
                                eventModel.getStartTime().getTime() / 1000,
                                0
                        );
                    }
                }));
    }
}
