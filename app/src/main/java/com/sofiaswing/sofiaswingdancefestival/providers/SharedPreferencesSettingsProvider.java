package com.sofiaswing.sofiaswingdancefestival.providers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.sofiaswing.sofiaswingdancefestival.providers.providerModels.EventSubscriptionModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.BehaviorSubject;

public class SharedPreferencesSettingsProvider implements ProvidersInterfaces.ISettingsProvider {
    private static final String NOT_TIME_SECS_SETTING_NAME = "NOTIFICATION_TIME_SETTING";
    private static final String NEWS_NOTIF_ENABL_SETTING_NAME = "NEWS_NOTIFICATION_SETTING";
    private static final String EVENT_NOTIFS_SETTING_NAME = "EVENT_NOTIFICATIONS_SETTING";
    private static final String EVENT_NOTIFS_DEFAULT_CLASS_LEVEL_NAME = "EVENT_NOTIFS_DEFAULT_CLASS_LEVEL";
    private static final String YEAR_FROM_DB_SETTING_NAME = "YEAR_FROM_DB_SETTING";
    private static final String CUSTOM_YEAR_SETTING_NAME = "CUSTOM_YEAR_SETTING";

    private final Context context;
    private final ProvidersInterfaces.ISerializer serializer;
    private final ProvidersInterfaces.IEventAlarmManager eventAlarmManager;

    private final MutableLiveData<Pair<Boolean, String>> ssdfYearLiveData = new MutableLiveData<>();

    public SharedPreferencesSettingsProvider(Context context, ProvidersInterfaces.ISerializer serializer, ProvidersInterfaces.IEventAlarmManager eventAlarmManager) {
        this.context = context;
        this.serializer = serializer;
        this.eventAlarmManager = eventAlarmManager;

        emmitSsdfYear();
    }

    @Override
    public boolean isSubscribedForEvent(String eventId) {
        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);

        return eventNotifRef.contains(eventId);
    }

    @Override
    public void subscribeForEvent(String eventId, String eventName, long startTimestamp) {
        this.subscribeForEvent(eventId, eventName, startTimestamp, this.getEventsNotificationAdvanceTimeSeconds());
    }

    @Override
    public void subscribeForEvent(String eventId, String eventName, long startTimestamp, long notifAdvanceTimeSeconds) {
        final EventSubscriptionModel newSubscrEvent =
                new EventSubscriptionModel(eventId, eventName, startTimestamp, notifAdvanceTimeSeconds);

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
                newSubscrEvent.getEventTimestamp() - newSubscrEvent.getNotifAdvanceTimeSeconds());
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
    public void updateEventSubscription(String eventId, String eventName, long startTimestamp, boolean useDefaultNotifTime) {
        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);

        final String oldNotifStr = eventNotifRef.getString(eventId, null);
        boolean shouldUpdateAlarm = false;
        long eventNotifAdvanceTimeSeconds = this.getEventsNotificationAdvanceTimeSeconds();
        if (eventId != null) {
            final EventSubscriptionModel oldSubscrEvent =
                    new EventSubscriptionModel(this.serializer.deserializeToMap(oldNotifStr));

            if (!useDefaultNotifTime) {
                eventNotifAdvanceTimeSeconds = oldSubscrEvent.getNotifAdvanceTimeSeconds();
            }

            if (oldSubscrEvent.getEventTimestamp() != startTimestamp
                || oldSubscrEvent.getNotifAdvanceTimeSeconds() != eventNotifAdvanceTimeSeconds) {
                shouldUpdateAlarm = true;
            }
        }

        final EventSubscriptionModel newSubscrEvent =
                new EventSubscriptionModel(eventId, eventName, startTimestamp, eventNotifAdvanceTimeSeconds);

        {
            final SharedPreferences.Editor eventNotifEditor = eventNotifRef.edit();

            eventNotifEditor.putString(
                    newSubscrEvent.getEventId(),
                    this.serializer.serialize(newSubscrEvent.toMap())
            );
            eventNotifEditor.commit();
        }

        // Update the alarm only if the notification time has changed
        if (shouldUpdateAlarm) {
            this.eventAlarmManager.setNotificationAlarmForFutureEvent(
                    newSubscrEvent.getEventId(),
                    newSubscrEvent.getEventName(),
                    newSubscrEvent.getEventTimestamp(),
                    newSubscrEvent.getEventTimestamp() - newSubscrEvent.getNotifAdvanceTimeSeconds());
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
    public void setDefaultNotificationTimeToAllEvents() {
        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);

        for (String eventId: eventNotifRef.getAll().keySet()) {
            EventSubscriptionModel oldEvent = new EventSubscriptionModel(
                    this.serializer.deserializeToMap(eventNotifRef.getString(eventId, "{}"))
            );

            long newEventNotifTimestamp =
                    oldEvent.getEventTimestamp() - this.getEventsNotificationAdvanceTimeSeconds();

            this.updateEventSubscription(
                    oldEvent.getEventId(),
                    oldEvent.getEventName(),
                    oldEvent.getEventTimestamp(),
                    true
            );
        }
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
                    event.getEventTimestamp() - event.getNotifAdvanceTimeSeconds());
        }
    }

    @Override
    public List<String> getSubscribedEventsIds() {
        final SharedPreferences eventNotifRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_SETTING_NAME,0);

        return new ArrayList<String>(eventNotifRef.getAll().keySet());
    }

    @Override
    public String getDefaultClassLevel() {
        final SharedPreferences defaultClassLevelRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_DEFAULT_CLASS_LEVEL_NAME,0);
        return defaultClassLevelRef.getString(EVENT_NOTIFS_DEFAULT_CLASS_LEVEL_NAME, "");
    }

    @Override
    public void setDefaultClassLevel(String classLevel) {
        final SharedPreferences defaultClassLevelRef =
                this.context.getSharedPreferences(EVENT_NOTIFS_DEFAULT_CLASS_LEVEL_NAME,0);
        final SharedPreferences.Editor defaultClassLevelEditor = defaultClassLevelRef.edit();

        defaultClassLevelEditor.putString(EVENT_NOTIFS_DEFAULT_CLASS_LEVEL_NAME, classLevel);
        defaultClassLevelEditor.commit();
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

    @Override
    public boolean isYearFromDatabase() {
        final SharedPreferences yearFromDbRef =
                this.context.getSharedPreferences(YEAR_FROM_DB_SETTING_NAME,0);
        return yearFromDbRef.getBoolean(YEAR_FROM_DB_SETTING_NAME, true);
    }

    @Override
    public String getCurrentCustomSsdfYear() {
        final SharedPreferences customYearRef =
                this.context.getSharedPreferences(CUSTOM_YEAR_SETTING_NAME,0);
        return customYearRef.getString(CUSTOM_YEAR_SETTING_NAME, "");
    }

    @Override
    public void setCurrentSsdfYearFromData() {
        final SharedPreferences yearFromDbRef =
                this.context.getSharedPreferences(YEAR_FROM_DB_SETTING_NAME,0);
        final SharedPreferences.Editor yearFromDbEditor = yearFromDbRef.edit();

        yearFromDbEditor.putBoolean(YEAR_FROM_DB_SETTING_NAME, true);
        yearFromDbEditor.commit();
        emmitSsdfYear();
    }

    @Override
    public void setCurrentSsdfYear(String currentSsdfYear) {
        final SharedPreferences customYearRef =
                this.context.getSharedPreferences(CUSTOM_YEAR_SETTING_NAME,0);
        final SharedPreferences.Editor customYearEditor = customYearRef.edit();

        customYearEditor.putString(CUSTOM_YEAR_SETTING_NAME, currentSsdfYear);
        customYearEditor.commit();

        final SharedPreferences yearFromDbRef =
                this.context.getSharedPreferences(YEAR_FROM_DB_SETTING_NAME,0);
        final SharedPreferences.Editor yearFromDbEditor = yearFromDbRef.edit();

        yearFromDbEditor.putBoolean(YEAR_FROM_DB_SETTING_NAME, false);
        yearFromDbEditor.commit();
        emmitSsdfYear();
    }

    @Override
    public LiveData<Pair<Boolean, String>> obsCurrentSsdfYear() {
        return this.ssdfYearLiveData;
    }

    private void emmitSsdfYear() {
        this.ssdfYearLiveData.postValue(
            new Pair<>(this.isYearFromDatabase(), this.getCurrentCustomSsdfYear())
        );
    }
}
