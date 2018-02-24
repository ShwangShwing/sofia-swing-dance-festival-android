package com.sofiaswing.sofiaswingdancefestival.views.map;

import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.List;

/**
 * Created by ivo on 16.02.18.
 */

public class MapsInterfaces {
  interface IView {
    void setVenues(List<VenueModel> venues);
  }
  interface IPresenter {
    void setView(IView view);
    void start();
    void stop();
  }
}
