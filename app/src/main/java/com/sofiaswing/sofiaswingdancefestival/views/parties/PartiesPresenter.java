package com.sofiaswing.sofiaswingdancefestival.views.parties;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
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
    private final DataInterfaces.IPartiesData partiesData;
    private final DataInterfaces.IVenuesData venuesData;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;
    private final List<PartyViewModel> partyViewModels;

    private final CompositeDisposable subscriptions;

    public PartiesPresenter(DataInterfaces.IPartiesData partiesData,
                            DataInterfaces.IVenuesData venuesData, ProvidersInterfaces.ISettingsProvider settingsProvider) {
        this.partiesData = partiesData;
        this.venuesData = venuesData;
        this.settingsProvider = settingsProvider;
        this.partyViewModels = new ArrayList<>();

        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public void setView(PartiesInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        this.subscriptions.add(
            this.partiesData.getParties()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(parties -> {
                    partyViewModels.clear();
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

                        this.venuesData.getById(party.getVenueId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<VenueModel>() {
                                    @Override
                                    public void accept(VenueModel venueModel) throws Exception {
                                        partyViewModel.setVenue(venueModel);
                                        view.setParties(partyViewModels);
                                    }
                                });
                    }
                    view.setParties(partyViewModels);
                }));
    }

    @Override
    public void stop() {
        this.subscriptions.clear();
    }

    @Override
    public void setPartySubscription(int position, boolean subscriptionStatus) {
        PartyViewModel party = partyViewModels.get(position);
        if (subscriptionStatus) {
            this.settingsProvider.subscribeForEvent(
                    party.getId(),
                    party.getName(),
                    party.getStartTime().getTime() / 1000, 0);
            party.setSubscribed(true);
        }
        else {
            this.settingsProvider.unsubscribeFromEvent(party.getId());
            party.setSubscribed(false);
        }
    }
}
