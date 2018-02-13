package com.sofiaswing.sofiaswingdancefestival.views.contactUs;

import java.util.List;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class ContactUsInterfaces {
    public interface IView {
        void setContacts(List<ContactViewModel> contacts);
    }

    public interface IPresenter {
        void setView(IView view);
        void start();
    }
}
