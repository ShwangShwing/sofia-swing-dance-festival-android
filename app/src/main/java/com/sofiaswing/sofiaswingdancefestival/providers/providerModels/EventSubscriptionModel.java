package com.sofiaswing.sofiaswingdancefestival.providers.providerModels;

import java.util.HashMap;
import java.util.Map;

public final class EventSubscriptionModel {
    private static final String EVENT_ID_MAP_KEY = "event_id";
    private static final String EVENT_NAME_MAP_KEY = "event_name";
    private static final String EVENT_TIMESTAMP_MAP_KEY = "event_timestamp";
    private static final String EVENT_NOTIFY_TIMESTAMP_MAP_KEY = "event_notify_timestamp";

    private final String eventId;
    private final String eventName;
    private final long eventTimestamp;
    private final long notifyTimestamp;

    public EventSubscriptionModel(String eventId, String eventName, long eventTimestamp, long notifyTimestamp) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventTimestamp = eventTimestamp;
        this.notifyTimestamp = notifyTimestamp;
    }

    public EventSubscriptionModel(Map<String, String> eventInMap) {
        this.eventId = eventInMap.get(EVENT_ID_MAP_KEY);
        if (this.eventId == null) throw new NullPointerException("No event id in map!");
        this.eventName = eventInMap.get(EVENT_NAME_MAP_KEY);
        if (this.eventName == null) throw new NullPointerException("No event name in map!");
        this.eventTimestamp = Long.parseLong(eventInMap.get(EVENT_TIMESTAMP_MAP_KEY));
        this.notifyTimestamp = Long.parseLong(eventInMap.get(EVENT_NOTIFY_TIMESTAMP_MAP_KEY));
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public long getNotifyTimestamp() {
        return notifyTimestamp;
    }

    public Map<String, String> toMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(EVENT_ID_MAP_KEY, eventId);
        result.put(EVENT_NAME_MAP_KEY, eventName);
        result.put(EVENT_TIMESTAMP_MAP_KEY, String.valueOf(eventTimestamp));
        result.put(EVENT_NOTIFY_TIMESTAMP_MAP_KEY, String.valueOf(notifyTimestamp));

        return result;
    }
}
