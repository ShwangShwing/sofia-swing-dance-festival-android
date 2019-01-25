package com.sofiaswing.sofiaswingdancefestival.providers.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.sofiaswing.sofiaswingdancefestival.providers.DefaultSettingValues;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.providerModels.EventSubscriptionModel;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesSettingsProvider implements ProvidersInterfaces.ISettingsProvider {
    private static final String NOT_TIME_SECS_SETTING_NAME = "NOTIFICATION_TIME_SETTING";
    private static final String NEWS_NOTIF_ENABL_SETTING_NAME = "NEWS_NOTIFICATION_SETTING";
    private static final String EVENT_NOTIFS_SETTING_NAME = "EVENT_NOTIFICATIONS_SETTING";

    private final Context context;
    private final ProvidersInterfaces.ISerializer serializer;
    private final ProvidersInterfaces.IEventAlarmManager eventAlarmManager;

    public SharedPreferencesSettingsProvider(Context context, ProvidersInterfaces.ISerializer serializer, ProvidersInterfaces.IEventAlarmManager eventAlarmManager) {
        this.context = context;
        this.serializer = serializer;
        this.eventAlarmManager = eventAlarmManager;
    }

    @Override
    public boolean isSubscribedForEvent(String eventId) {
        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);

        return eventNotifRef.contains(eventId);
    }

    @Override
    public void subscribeForEvent(String eventId, String eventName, long startTimestamp, long notifyTimestamp) {
        final EventSubscriptionModel newSubscrEvent =
                new EventSubscriptionModel(eventId, eventName, startTimestamp, notifyTimestamp);

        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);
        final SharedPreferences.Editor eventNotifEditor = eventNotifRef.edit();

        eventNotifEditor.putString(
                newSubscrEvent.getEventId(),
                this.serializer.serialize(newSubscrEvent.toMap())
        );
        eventNotifEditor.commit();

        this.eventAlarmManager.setNotificationAlarmForFutureEvent(
                newSubscrEvent.getEventId(),
                newSubscrEvent.getEventName(),
                newSubscrEvent.getEventTimestamp(),
                newSubscrEvent.getNotifyTimestamp());
    }

    @Override
    public void unsubscribeFromEvent(String eventId) {
        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);
        final SharedPreferences.Editor eventNotifEditor = eventNotifRef.edit();

        eventNotifEditor.remove(eventId);
        eventNotifEditor.commit();

        this.eventAlarmManager.cancelNotificationAlarm(eventId);
    }

    @Override
    public void updateEventSubscription(String eventId, String eventName, long startTimestamp, long notifyTimestamp) {
        final EventSubscriptionModel newSubscrEvent =
                new EventSubscriptionModel(eventId, eventName, startTimestamp, notifyTimestamp);

        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);

        final String oldNotifStr = eventNotifRef.getString(eventId, null);
        boolean isNotifTimeChanged = false;
        if (eventId != null) {
            final EventSubscriptionModel oldSubscrEvent =
                    new EventSubscriptionModel(this.serializer.deserializeToMap(oldNotifStr));
            if (oldSubscrEvent.getNotifyTimestamp() != newSubscrEvent.getNotifyTimestamp()) {
                isNotifTimeChanged = true;
            }
        }

        {
            final SharedPreferences.Editor eventNotifEditor = eventNotifRef.edit();

            eventNotifEditor.putString(
                    newSubscrEvent.getEventId(),
                    this.serializer.serialize(newSubscrEvent.toMap())
            );
            eventNotifEditor.commit();
        }

        // Update the alarm only if the notification time has changed
        if (isNotifTimeChanged) {
            this.eventAlarmManager.setNotificationAlarmForFutureEvent(
                    newSubscrEvent.getEventId(),
                    newSubscrEvent.getEventName(),
                    newSubscrEvent.getEventTimestamp(),
                    newSubscrEvent.getNotifyTimestamp());
        }
    }

    @Override
    public long getEventsNotificationAdvanceTimeSeconds() {
        final SharedPreferences notTimeRef =
                this.context.getSharedPreferences(NOT_TIME_SECS_SETTING_NAME,0);
        return notTimeRef.getLong(NOT_TIME_SECS_SETTING_NAME, DefaultSettingValues.EVENT_NOTIFICATION_TIME_S);
    }

    @Override
    public void setEventsNotificationAdvanceTimeSeconds(long seconds) {
        final SharedPreferences notTimeRef =
                this.context.getSharedPreferences(NOT_TIME_SECS_SETTING_NAME,0);
        final SharedPreferences.Editor notTimeEditor = notTimeRef.edit();

        notTimeEditor.putLong(NOT_TIME_SECS_SETTING_NAME, seconds);
        notTimeEditor.commit();
    }

    @Override
    public void setupAllNotificationAlarms() {
        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);

        for (String eventId: eventNotifRef.getAll().keySet()) {
            EventSubscriptionModel event = new EventSubscriptionModel(
                    this.serializer.deserializeToMap(eventNotifRef.getString(eventId, "{}"))
            );
            this.eventAlarmManager.setNotificationAlarmForFutureEvent(
                    event.getEventId(),
                    event.getEventName(),
                    event.getEventTimestamp(),
                    event.getNotifyTimestamp());
        }
    }

    @Override
    public List<String> getSubscribedEventsIds() {
        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);

        return new ArrayList<String>(eventNotifRef.getAll().keySet());
    }

    @Override
    public boolean areNewsNotificationsEnabled() {
        final SharedPreferences newsNotRef =
                this.context.getSharedPreferences(NEWS_NOTIF_ENABL_SETTING_NAME,0);
        return newsNotRef.getBoolean(NEWS_NOTIF_ENABL_SETTING_NAME, DefaultSettingValues.IS_NEWS_NOTIF_ENABLED);
    }

    @Override
    public void enableNewsNotifications() {
        this.setNewsNotificationValue(true);
    }

    @Override
    public void disableNewsNotifications() {
        this.setNewsNotificationValue(false);
    }

    private void setNewsNotificationValue(final boolean areNewsNotificationsEnabled) {
        final SharedPreferences newsNotRef =
                this.context.getSharedPreferences(NEWS_NOTIF_ENABL_SETTING_NAME,0);
        final SharedPreferences.Editor notTimeEditor = newsNotRef.edit();

        notTimeEditor.putBoolean(NEWS_NOTIF_ENABL_SETTING_NAME, areNewsNotificationsEnabled);
        notTimeEditor.commit();
    }
}
