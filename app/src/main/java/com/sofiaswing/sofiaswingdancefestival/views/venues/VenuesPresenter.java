package com.sofiaswing.sofiaswingdancefestival.views.venues;

import android.location.Location;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
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

    public VenuesPresenter(VenuesInterfaces.IView view, DataInterfaces.IVenuesData venuesData,
                           ProvidersInterfaces.ILocationProvider locationProvider) {
        this.venuesData = venuesData;
        this.view = view;
        this.view.setPresenter(this);
        this.locationProvider = locationProvider;
        this.view.setLocationProvider(locationProvider);
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
                        List<VenueViewModel> venuesForView = new ArrayList<VenueViewModel>();

                        for (VenueModel venue : venueModels) {
                            Location location = null;
                            if (venue.getLocation() != null) {
                                location = new Location(venue.getLocation());
                            }
                            venuesForView.add(new VenueViewModel(
                                    venue.getName(),
                                    venue.getAddress(),
                                    location
                            ));
                        }

                        view.setVenues(venuesForView);
                    }
                });
    }
}
