package com.sofiaswing.sofiaswingdancefestival.views.venues;

import android.location.Location;

import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.List;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenuesInterfaces {
    interface IView {
        void setVenues(List<VenueModel> venues);
        void setLocation(Location location);
    }
    interface IPresenter {
        void setView(IView view);
        void start();
        void stop();
    }
}
