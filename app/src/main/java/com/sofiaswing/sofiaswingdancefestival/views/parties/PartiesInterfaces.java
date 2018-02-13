package com.sofiaswing.sofiaswingdancefestival.views.parties;

import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;

import java.util.List;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class PartiesInterfaces {
    public interface IView {
        void setParties(List<PartyViewModel> parties);
    }

    public interface IPresenter {
        void setView(IView view);
        void start();
        void stop();
        void setPartySubscription(int position, boolean subscriptionStatus);
    }
}
