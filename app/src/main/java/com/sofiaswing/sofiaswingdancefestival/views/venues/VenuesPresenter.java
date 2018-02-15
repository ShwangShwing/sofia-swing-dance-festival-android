package com.sofiaswing.sofiaswingdancefestival.views.venues;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenuesPresenter implements VenuesInterfaces.IPresenter {
    private VenuesInterfaces.IView view;
    private final DataInterfaces.IVenuesData venuesData;
    private final ProvidersInterfaces.ILocationProvider locationProvider;
    private final CompositeDisposable subscriptions;

    public VenuesPresenter(DataInterfaces.IVenuesData venuesData,
                           ProvidersInterfaces.ILocationProvider locationProvider) {
        this.venuesData = venuesData;
        this.locationProvider = locationProvider;

        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public void setView(VenuesInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        this.locationProvider.startLocationService();
        subscribeForVenuesData();
        subscribeForLocation();
    }

    private void subscribeForVenuesData() {
        subscriptions.add(this.venuesData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(venueModels -> view.setVenues(venueModels)));
    }

    private void subscribeForLocation() {
        subscriptions.add(this.locationProvider.getCurrentLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentLocation -> view.setLocation(currentLocation)));
    }

    @Override
    public void stop() {
        this.subscriptions.clear();
        this.locationProvider.stopLocationService();
    }
}
