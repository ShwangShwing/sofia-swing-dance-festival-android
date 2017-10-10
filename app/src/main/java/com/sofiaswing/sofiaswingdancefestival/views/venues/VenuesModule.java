package com.sofiaswing.sofiaswingdancefestival.views.venues;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/10/17.
 */

@Module
public class VenuesModule {
    @Provides
    VenuesInterfaces.IView provideView() {
        return new VenuesView();
    }

    @Provides
    VenuesInterfaces.IPresenter providePresenter() {
        return new VenuesPresenter();
    }
}
