package com.sofiaswing.sofiaswingdancefestival.views.parties;

import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.Date;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class PartyViewModel {
    private final String id;
    private final Date startTime;
    private final Date endTime;
    private final String name;
    private VenueModel venue;
    private boolean isSubscribed;

    public PartyViewModel(String id, Date startTime, Date endTime, String name, VenueModel venue, boolean isSubscribed) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.venue = venue;
        this.isSubscribed = isSubscribed;
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
    public void setVenue(VenueModel venue) {
        this.venue = venue;
    }

    public boolean isSubscribed() {
        return this.isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}
