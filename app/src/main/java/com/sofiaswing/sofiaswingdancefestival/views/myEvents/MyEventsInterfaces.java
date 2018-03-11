package com.sofiaswing.sofiaswingdancefestival.views.myEvents;

import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.List;

/**
 * Created by shwangshwing on 3/5/18.
 */

public class MyEventsInterfaces {
    public interface IView {
        void setClassLevelString(String levelId, String classLevelString);

        void setEvents(List<EventViewModel> events);

        void setEventVenue(int eventPosition, VenueModel venueModel);

        void setEventSubscriptionState(int position, boolean subscriptionStatus);
    }

    public interface IPresenter {
        void setView(IView view);
        void start();
        void stop();

        void setEventSubscription(int position, boolean subscriptionStatus);
    }
}
