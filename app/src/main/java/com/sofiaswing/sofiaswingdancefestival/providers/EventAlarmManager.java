package com.sofiaswing.sofiaswingdancefestival.providers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sofiaswing.sofiaswingdancefestival.utils.EventSubscriptionAlarmReceiver;

public final class EventAlarmManager implements ProvidersInterfaces.IEventAlarmManager {
    private final Context context;
    private final ProvidersInterfaces.ICurrentTimeProvider timeProvider;

    public EventAlarmManager(Context context, ProvidersInterfaces.ICurrentTimeProvider timeProvider) {
        this.context = context;
        this.timeProvider = timeProvider;
    }

    @Override
    public void setNotificationAlarmForFutureEvent(String eventId, String eventName, long startTimestamp, long notifyTimestamp) {
        this.cancelNotificationAlarm(eventId);
        if (this.timeProvider.getCurrentTimeMs() / 1000 > startTimestamp) {
            // The event has already started. Don't notify.
            return;
        }

        Intent eventNotificationIntent = new Intent(this.context, EventSubscriptionAlarmReceiver.class);
        eventNotificationIntent.putExtra(EventSubscriptionAlarmReceiver.EVENT_ID_KEY, eventId);
        eventNotificationIntent.putExtra(EventSubscriptionAlarmReceiver.EVENT_NAME_KEY, eventName);
        eventNotificationIntent.putExtra(EventSubscriptionAlarmReceiver.EVENT_START_TIME_KEY, startTimestamp);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        this.context,
                        eventId.hashCode(),
                        eventNotificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        long executionTimestampMs = notifyTimestamp * 1000;
        AlarmManager alarmManager = (AlarmManager)this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, executionTimestampMs, pendingIntent);
    }

    @Override
    public void cancelNotificationAlarm(String eventId) {
        Intent eventNotificationIntent = new Intent(this.context, EventSubscriptionAlarmReceiver.class);
        eventNotificationIntent.putExtra(EventSubscriptionAlarmReceiver.EVENT_ID_KEY, eventId);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        this.context,
                        eventId.hashCode(),
                        eventNotificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
