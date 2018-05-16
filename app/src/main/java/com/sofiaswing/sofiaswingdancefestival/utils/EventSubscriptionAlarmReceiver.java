package com.sofiaswing.sofiaswingdancefestival.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.main.MainActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

public class EventSubscriptionAlarmReceiver extends BroadcastReceiver {
    public static final String EVENT_ID_KEY = "event_id_key";
    public static final String EVENT_NAME_KEY = "event_name_key";
    public static final String EVENT_START_TIME_KEY = "event_start_time_key";
    public static final String EVENT_REMINDER_CHANNEL_NAME = "event_reminder";

    @Inject
    ProvidersInterfaces.ISettingsProvider settingsProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())
                || "android.intent.action.QUICKBOOT_POWERON".equals(intent.getAction())) {
            this.inject(context);

            // Reset alarms on reboot
            this.settingsProvider.setupAllNotificationAlarms();

            return;
        }

        String eventId = intent.getStringExtra(EVENT_ID_KEY);
        String eventName = intent.getStringExtra(EVENT_NAME_KEY);

        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Sofia"));
        long eventStartTimeIn = intent.getLongExtra(EVENT_START_TIME_KEY, 0);
        String eventStartTime = "Start time unknown! Error!";
        if (eventStartTimeIn > 0) {
            eventStartTime = dateFormatter.format(new Date((long) eventStartTimeIn * 1000));
        }

        Intent resultIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        resultIntent.putExtra(EVENT_ID_KEY, eventId);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, eventId.hashCode(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = NotificationCreator.getNotification(
                context, EVENT_REMINDER_CHANNEL_NAME, eventName, eventStartTime, pendingIntent);

        notificationManager.notify(eventId.hashCode(), notification);
    }

    private void inject(Context context) {
        SofiaSwingDanceFestivalApplication app = (SofiaSwingDanceFestivalApplication) context.getApplicationContext();
        app.getComponent().inject(this);
    }
}
