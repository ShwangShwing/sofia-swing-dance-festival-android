package com.sofiaswing.sofiaswingdancefestival.views.venues;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/10/17.
 */

@Module
public class VenuesModule {
    @Provides
    VenuesInterfaces.IPresenter providePresenter(
            DataInterfaces.IVenuesData venuesData,
            ProvidersInterfaces.ILocationProvider locationProvider) {
        return new VenuesPresenter(venuesData, locationProvider);
    }
}
