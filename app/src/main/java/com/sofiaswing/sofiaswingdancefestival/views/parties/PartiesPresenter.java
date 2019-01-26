package com.sofiaswing.sofiaswingdancefestival.views.parties;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class PartiesPresenter implements PartiesInterfaces.IPresenter {
    private PartiesInterfaces.IView view;
    private final DataInterfaces.IEventsData partiesData;
    private final DataInterfaces.IVenuesData venuesData;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;
    private final ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider;
    private final List<PartyViewModel> partyViewModels;

    private final CompositeDisposable partiesSubscriptions;
    private final CompositeDisposable venuesSubscriptions;

    public PartiesPresenter(DataInterfaces.IEventsData partiesData,
                            DataInterfaces.IVenuesData venuesData,
                            ProvidersInterfaces.ISettingsProvider settingsProvider,
                            ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider) {
        this.partiesData = partiesData;
        this.venuesData = venuesData;
        this.settingsProvider = settingsProvider;
        this.currentTimeProvider = currentTimeProvider;
        this.partyViewModels = new ArrayList<>();

        this.partiesSubscriptions = new CompositeDisposable();
        this.venuesSubscriptions = new CompositeDisposable();
    }

    @Override
    public void setView(PartiesInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        this.view.setCurrentTimestampMs(this.currentTimeProvider.getCurrentTimeMs());
        this.partiesSubscriptions.add(
            this.partiesData.getParties()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(parties -> {
                    partyViewModels.clear();
                    venuesSubscriptions.clear();
                    for (PartyModel party : parties) {
                        final PartyViewModel partyViewModel = new PartyViewModel(
                                party.getId(),
                                party.getStartTime(),
                                party.getEndTime(),
                                party.getName(),
                                null,
                                settingsProvider.isSubscribedForEvent(party.getId())
                        );
                        partyViewModels.add(partyViewModel);
                        final int partyPosition = partyViewModels.size() - 1;

                        venuesSubscriptions.add(this.venuesData.getById(party.getVenueId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<VenueModel>() {
                                    @Override
                                    public void accept(VenueModel venueModel) throws Exception {
                                        view.setEventVenue(partyPosition, venueModel);
                                    }
                                }));
                    }
                    view.setParties(partyViewModels);
                }));
    }

    @Override
    public void stop() {
        this.partiesSubscriptions.clear();
        this.venuesSubscriptions.clear();
    }

    @Override
    public void setPartySubscription(int position, boolean subscriptionStatus) {
        PartyViewModel party = partyViewModels.get(position);
        if (subscriptionStatus) {
            Date startTime = party.getStartTime();
            if (startTime != null) {
                this.settingsProvider.subscribeForEvent(
                        party.getId(),
                        party.getName(),
                        party.getStartTime().getTime() / 1000);
            }
        }
        else {
            this.settingsProvider.unsubscribeFromEvent(party.getId());
        }

        this.view.setEventSubscriptionState(position, subscriptionStatus);
    }
}
