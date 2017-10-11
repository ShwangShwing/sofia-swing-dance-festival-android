package com.sofiaswing.sofiaswingdancefestival.views.parties;

import com.sofiaswing.sofiaswingdancefestival.models.PartyModel;

import java.util.List;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class PartiesInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setParties(List<PartyViewModel> parties);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void setPartySubscription(int position, boolean subscriptionStatus);
    }
}
