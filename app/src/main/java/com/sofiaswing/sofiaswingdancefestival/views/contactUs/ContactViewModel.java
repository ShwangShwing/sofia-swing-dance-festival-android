package com.sofiaswing.sofiaswingdancefestival.views.contactUs;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class ContactViewModel {
    final private String name;
    final private String phoneNumber;

    public ContactViewModel(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
