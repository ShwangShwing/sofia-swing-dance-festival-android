package com.sofiaswing.sofiaswingdancefestival.views.parties;

import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.List;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class PartiesInterfaces {
    public interface IView {
        void setParties(List<PartyViewModel> parties);

        void setEventVenue(int position, VenueModel venue);

        void setEventSubscriptionState(int position, boolean isSubscribed);
    }

    public interface IPresenter {
        void setView(IView view);
        void start();
        void stop();
        void setPartySubscription(int position, boolean subscriptionStatus);
    }
}
