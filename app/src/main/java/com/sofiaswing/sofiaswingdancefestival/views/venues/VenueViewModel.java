package com.sofiaswing.sofiaswingdancefestival.views.venues;

import android.location.Location;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenueViewModel {
    private final String name;
    private final String address;
    private final Location location;

    public VenueViewModel(String name, String address, Location coordinates) {
        this.name = name;
        this.address = address;
        this.location = coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

}
