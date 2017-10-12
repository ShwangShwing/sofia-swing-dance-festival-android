package com.sofiaswing.sofiaswingdancefestival.views.contactUs;

import java.util.List;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class ContactUsInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);

        void setContacts(List<ContactViewModel> contacts);
    }

    public interface IPresenter {
        IView getView();
        void start();
    }
}
