package com.sofiaswing.sofiaswingdancefestival.views.venues;

import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.List;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenuesInterfaces {
    interface IView {
        void setPresenter(IPresenter presenter);

        void setVenues(List<VenueModel> venues);

        void setLocationProvider(ProvidersInterfaces.ILocationProvider locationProvider);
    }
    interface IPresenter {
        IView getView();
        void start();
        void stop();
    }
}
