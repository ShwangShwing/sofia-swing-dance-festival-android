package com.sofiaswing.sofiaswingdancefestival.views.venues;

import android.location.Location;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenuesPresenter
        implements VenuesInterfaces.IPresenter {
    private final VenuesInterfaces.IView view;
    private final DataInterfaces.IVenuesData venuesData;
    private final ProvidersInterfaces.ILocationProvider locationProvider;
    private final CompositeDisposable subscriptions;

    public VenuesPresenter(VenuesInterfaces.IView view, DataInterfaces.IVenuesData venuesData,
                           ProvidersInterfaces.ILocationProvider locationProvider) {
        this.venuesData = venuesData;
        this.view = view;
        this.view.setPresenter(this);
        this.locationProvider = locationProvider;
        this.view.setLocationProvider(locationProvider);

        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public VenuesInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.venuesData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VenueModel>>() {
                    @Override
                    public void accept(List<VenueModel> venueModels) throws Exception {
                        view.setVenues(venueModels);
                    }
                });
    }

    @Override
    public void stop() {
        this.subscriptions.clear();
    }
}
