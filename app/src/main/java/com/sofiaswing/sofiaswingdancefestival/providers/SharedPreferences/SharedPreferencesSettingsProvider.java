package com.sofiaswing.sofiaswingdancefestival.providers.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.sofiaswing.sofiaswingdancefestival.providers.DefaultSettingValues;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.List;

public class SharedPreferencesSettingsProvider implements ProvidersInterfaces.ISettingsProvider {
    private static final String NOT_TIME_SECS_SETTING_NAME = "NOTIFICATION_TIME_SETTING";
    private static final String NEWS_NOTIF_ENABL_SETTING_NAME = "NEWS_NOTIFICATION_SETTING";

    private final Context context;

    public SharedPreferencesSettingsProvider(Context context) {
        this.context = context;
    }

    @Override
    public boolean isSubscribedForEvent(String eventId) {
        return false;
    }

    @Override
    public void subscribeForEvent(String eventId, String eventName, long startTimestamp, long notifyTimestamp) {

    }

    @Override
    public void unsubscribeFromEvent(String eventId) {

    }

    @Override
    public void updateEventSubscription(String eventId, String eventName, long startTimestamp, long notifyTimestamp) {

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

    }

    @Override
    public List<String> getSubscribedEventsIds() {
        return null;
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
