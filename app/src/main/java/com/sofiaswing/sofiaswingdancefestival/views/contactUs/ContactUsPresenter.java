package com.sofiaswing.sofiaswingdancefestival.views.contactUs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class ContactUsPresenter implements ContactUsInterfaces.IPresenter {
    private ContactUsInterfaces.IView view;

    public ContactUsPresenter() {

    }

    @Override
    public void setView(ContactUsInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        List<ContactViewModel> contacts = new ArrayList<>();
        contacts.add(new ContactViewModel("Yavor Kunchev", "+359896878308"));

        this.view.setContacts(contacts);
    }
}
