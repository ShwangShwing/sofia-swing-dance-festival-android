package com.sofiaswing.sofiaswingdancefestival.providers;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Pair;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.ui.UiInterfaces;

import java.util.List;

public class SettingsProviderPopupOnSubscribeDecorator implements ProvidersInterfaces.ISettingsProvider {
    private final ProvidersInterfaces.ISettingsProvider settingsProvider;
    private final UiInterfaces.IPopupCreator popupCreator;
    private final Context context;

    public SettingsProviderPopupOnSubscribeDecorator(
            ProvidersInterfaces.ISettingsProvider settingsProvider,
            UiInterfaces.IPopupCreator popupCreator,
            Context context) {
        this.settingsProvider = settingsProvider;
        this.popupCreator = popupCreator;
        this.context = context;
    }


    @Override
    public boolean isSubscribedForEvent(String eventId) {
        return this.settingsProvider.isSubscribedForEvent(eventId);
    }

    @Override
    public void subscribeForEvent(String eventId, String eventName, long startTimestamp) {
        this.settingsProvider.subscribeForEvent(eventId, eventName, startTimestamp);
        String message = this.context.getString(R.string.you_will_be_notified_before_event, eventName);
        this.popupCreator.popup(message);
    }

    @Override
    public void subscribeForEvent(String eventId, String eventName, long startTimestamp, long notifAdvanceTimeSeconds) {
        this.settingsProvider.subscribeForEvent(eventId, eventName, startTimestamp, notifAdvanceTimeSeconds);
        String message = this.context.getString(R.string.you_will_be_notified_before_event, eventName);
        this.popupCreator.popup(message);
    }

    @Override
    public void updateEventSubscription(String eventId, String eventName, long startTimestamp, boolean useDefaultNotifTime) {
        this.settingsProvider.updateEventSubscription(eventId, eventName, startTimestamp, useDefaultNotifTime);
    }

    @Override
    public void unsubscribeFromEvent(String eventId) {
        this.settingsProvider.unsubscribeFromEvent(eventId);
        String message = this.context.getString(R.string.you_wont_be_notified_about_event);
        this.popupCreator.popup(message);
    }

    @Override
    public long getEventsNotificationAdvanceTimeSeconds() {
        return this.settingsProvider.getEventsNotificationAdvanceTimeSeconds();
    }

    @Override
    public void setEventsNotificationAdvanceTimeSeconds(long seconds) {
        this.settingsProvider.setEventsNotificationAdvanceTimeSeconds(seconds);
    }

    @Override
    public void setDefaultNotificationTimeToAllEvents() {
        this.settingsProvider.setDefaultNotificationTimeToAllEvents();
    }

    @Override
    public void setupAllNotificationAlarms() {
        this.settingsProvider.setupAllNotificationAlarms();
    }

    @Override
    public List<String> getSubscribedEventsIds() {
        return this.settingsProvider.getSubscribedEventsIds();
    }

    @Override
    public boolean areNewsNotificationsEnabled() {
        return this.settingsProvider.areNewsNotificationsEnabled();
    }

    @Override
    public void enableNewsNotifications() {
        this.settingsProvider.enableNewsNotifications();
    }

    @Override
    public void disableNewsNotifications() {
        this.settingsProvider.disableNewsNotifications();
    }

    @Override
    public boolean isYearFromDatabase() {
        return this.settingsProvider.isYearFromDatabase();
    }

    @Override
    public String getCurrentCustomSsdfYear() {
        return this.settingsProvider.getCurrentCustomSsdfYear();
    }

    @Override
    public void setCurrentSsdfYearFromData() {
        this.settingsProvider.setCurrentSsdfYearFromData();
    }

    @Override
    public void setCurrentSsdfYear(String currentSsdfYear) {
        this.settingsProvider.setCurrentSsdfYear(currentSsdfYear);
    }

    @Override
    public LiveData<Pair<Boolean, String>> obsCurrentSsdfYear() {
        return this.settingsProvider.obsCurrentSsdfYear();
    }
}
