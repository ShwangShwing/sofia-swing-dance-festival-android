package com.sofiaswing.sofiaswingdancefestival.models;

import java.util.Date;

/**
 * Created by shwangshwing on 3/5/18.
 */

public class EventModel {
    private final String id;
    private final Date startTime;
    private final Date endTime;
    private final String name;
    private final String venueId;
    private final String eventType;

    public EventModel(String id, Date startTime, Date endTime, String name, String venueId, String eventType) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.venueId = venueId;
        this.eventType = eventType;
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

    public String getVenueId() {
        return venueId;
    }

    public String getEventType() {
        return eventType;
    }
}
