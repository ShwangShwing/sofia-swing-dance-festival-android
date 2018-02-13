package com.sofiaswing.sofiaswingdancefestival.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.main.MainActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class EventSubscriptionAlarmReceiver extends BroadcastReceiver {
    public static final String EVENT_ID_KEY = "event_id_key";
    public static final String EVENT_NAME_KEY = "event_name_key";
    public static final String EVENT_START_TIME_KEY = "event_start_time_key";

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
        long eventStartTimeIn = intent.getLongExtra(EVENT_START_TIME_KEY, 0);
        String eventStartTime = "Start time unknown! Error!";
        if (eventStartTimeIn > 0) {
            eventStartTime = dateFormatter.format(new Date((long) eventStartTimeIn * 1000));
        }

        // The id of the channel.
        String channelId = eventId;
        final long[] shaveAndHaircutVibPattern = new long[]{
                0,
                230, 20,
                105, 20,
                105, 20,
                230, 20,
                230, 20
                + 250,
                230, 20,
                230, 20
        };

        Intent resultIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        resultIntent.putExtra(EVENT_ID_KEY, eventId);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, eventId.hashCode(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.sofiaswinglogo)
                        .setBadgeIconType(R.drawable.sofiaswinglogo)
                        .setContentTitle(eventName)
                        .setContentText(eventStartTime)
                        .setVibrate(shaveAndHaircutVibPattern)
                        .setContentIntent(pendingIntent)
                        .setSound(
                                Uri.parse(
                                        String.format(
                                                "android.resource://%s/%s", context.getPackageName(),
                                                R.raw.shave_and_a_haricut)))
                        .setLights(Color.YELLOW, 2000, 3000)
                        .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(eventId.hashCode(), notificationBuilder.build());
    }

    private void inject(Context context) {
        SofiaSwingDanceFestivalApplication app = (SofiaSwingDanceFestivalApplication) context.getApplicationContext();
        app.getComponent().inject(this);
    }
}
