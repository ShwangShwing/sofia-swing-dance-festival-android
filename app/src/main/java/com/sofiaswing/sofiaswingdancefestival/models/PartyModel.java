package com.sofiaswing.sofiaswingdancefestival.models;

import java.util.Date;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class PartyModel {
    private final String id;
    private final Date startTime;
    private final Date endTime;
    private final String name;
    private final VenueModel venue;

    public PartyModel(String id, Date startTime, Date endTime, String name, VenueModel venue) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.venue = venue;
    }

    public String getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getName() {
        return name;
    }

    public VenueModel getVenue() {
        return venue;
    }
}