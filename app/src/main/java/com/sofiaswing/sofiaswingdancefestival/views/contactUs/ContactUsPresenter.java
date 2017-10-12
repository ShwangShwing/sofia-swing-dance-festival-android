package com.sofiaswing.sofiaswingdancefestival.views.contactUs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class ContactUsPresenter implements ContactUsInterfaces.IPresenter {
    private final ContactUsInterfaces.IView view;

    public ContactUsPresenter(ContactUsInterfaces.IView view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public ContactUsInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        List<ContactViewModel> contacts = new ArrayList<>();
        contacts.add(new ContactViewModel("Yavor Kunchev", "+359896878308"));

        this.view.setContacts(contacts);
    }
}
