package com.sofiaswing.sofiaswingdancefestival.models;

import android.location.Location;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class VenueModel {
    final String id;
    final String name;
    final String address;
    final Location location;
    private String distance = "-";

    public VenueModel(String id, String name, String address, Location location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public String getId() {
        return id;
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

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

}
