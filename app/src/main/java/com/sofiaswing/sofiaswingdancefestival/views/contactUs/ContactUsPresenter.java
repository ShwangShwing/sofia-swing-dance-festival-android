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
        contacts.add(new ContactViewModel("Yavor", "+359888222011"));
        contacts.add(new ContactViewModel("Damyan", "+359888680233"));
        contacts.add(new ContactViewModel("Sonia", "+359888615441"));

        this.view.setContacts(contacts);
    }
}
