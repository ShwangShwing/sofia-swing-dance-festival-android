package com.sofiaswing.sofiaswingdancefestival.views.schedule;

import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.util.Date;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class ScheduleEventViewModel {
    private final String id;
    private final String eventType;
    private final Date startTime;
    private final Date endTime;
    private final String name;
    private final String venueId;
    private boolean isSubscribed;

    public ScheduleEventViewModel(String id,
                                  String eventType,
                                  Date startTime,
                                  Date endTime, String name,
                                  String venueId,
                                  boolean isSubscribed) {
        this.id = id;
        this.eventType = eventType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.venueId = venueId;
        this.isSubscribed = isSubscribed;
    }

    public String getId() {
        return id;
    }

    public String getEventType() {
        return this.eventType;
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

    public String getVenueId() {
        return venueId;
    }

    public boolean isSubscribed() {
        return this.isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}
