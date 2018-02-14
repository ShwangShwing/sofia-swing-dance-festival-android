package com.sofiaswing.sofiaswingdancefestival.views.venues;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenuesPresenter
        implements VenuesInterfaces.IPresenter {
    private VenuesInterfaces.IView view;
    private final DataInterfaces.IVenuesData venuesData;
    private final ProvidersInterfaces.ILocationProvider locationProvider;
    private final CompositeDisposable subscriptions;
    private List<VenueModel> venues;

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
        this.locationProvider.startLocationService(((Fragment)view).getActivity());
        subscribeForVenuesData();
        subscribeForDistance();
    }

    private void subscribeForVenuesData() {
        subscriptions.add(this.venuesData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(venueModels -> {
                    view.setVenues(venueModels);
                    venues = venueModels;
                }));
    }

    private void subscribeForDistance() {
        Disposable d = locationProvider.getCurrentLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentLocation -> {
                    if (venues != null) {
                        for (VenueModel venue : venues) {
                            float distanceInMeters = currentLocation.distanceTo(venue.getLocation());
                            String formatedDistance = String.format("%.0fm", distanceInMeters);
                            if (distanceInMeters >= 1000) {
                                formatedDistance = String.format("%.1fkm", distanceInMeters / 1000);
                            }
                            venue.setDistance(formatedDistance);
                        }
                        view.setVenues(venues);
                    }
                });
        subscriptions.add(d);
    }

    @Override
    public void stop() {
        this.subscriptions.clear();
        this.locationProvider.stopLocationService();
    }
}
