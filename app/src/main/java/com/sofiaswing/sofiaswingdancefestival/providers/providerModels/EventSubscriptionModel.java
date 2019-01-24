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
        // Important! Don't throw any exceptions here because a possible bug
        // where an invalid user settings are saved may cause the application to
        // always crash on startup, attempting to load the invalid settings.
        String inEventId = eventInMap.get(EVENT_ID_MAP_KEY);
        if (inEventId == null) inEventId = "error-event-id-not-set";
        this.eventId = inEventId;
        String inEventName = eventInMap.get(EVENT_NAME_MAP_KEY);
        if (inEventName == null) inEventName = "error-event-name-not-set";
        this.eventName = inEventName;
        long inEventTimestamp = 0;
        long inNotifyTimestamp = 0;
        try {
            inEventTimestamp = Long.parseLong(eventInMap.get(EVENT_TIMESTAMP_MAP_KEY));
            inNotifyTimestamp = Long.parseLong(eventInMap.get(EVENT_NOTIFY_TIMESTAMP_MAP_KEY));
        }
        catch (NumberFormatException e) {}
        this.eventTimestamp = inEventTimestamp;
        this.notifyTimestamp = inNotifyTimestamp;
        // TODO: log an error in any of these cases.
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
