package com.sofiaswing.sofiaswingdancefestival.views.parties;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class PartiesPresenter implements PartiesInterfaces.IPresenter {
    private final PartiesInterfaces.IView view;
    private final DataInterfaces.IPartiesData partiesData;
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;
    private final List<PartyViewModel> partyViewModels;

    public PartiesPresenter(PartiesInterfaces.IView view,
                            DataInterfaces.IPartiesData partiesData,
                            ProvidersInterfaces.ISettingsProvider settingsProvider) {
        this.view = view;
        this.partiesData = partiesData;
        this.settingsProvider = settingsProvider;
        this.view.setPresenter(this);
        this.partyViewModels = new ArrayList<>();
    }

    @Override
    public PartiesInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.partiesData.getParties()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PartyModel>>() {
                    @Override
                    public void accept(List<PartyModel> parties) throws Exception {
                        partyViewModels.clear();
                        for (PartyModel party : parties) {
                            partyViewModels.add(new PartyViewModel(
                                    party.getId(),
                                    party.getStartTime(),
                                    party.getEndTime(),
                                    party.getName(),
                                    party.getVenue(),
                                    settingsProvider.isSubscribedForEvent(party.getId())
                            ));
                        }
                        view.setParties(partyViewModels);
                    }
                });
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
