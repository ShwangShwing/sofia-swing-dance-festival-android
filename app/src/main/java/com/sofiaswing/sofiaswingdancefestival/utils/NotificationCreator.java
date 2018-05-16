package com.sofiaswing.sofiaswingdancefestival.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.sofiaswing.sofiaswingdancefestival.R;

public class NotificationCreator {
    public static Notification getNotification(
            Context context,
            String channelId,
            String notificationTitle,
            String notificationText,
            PendingIntent contentIntent) {
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

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.sofia_swing_logo)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setVibrate(shaveAndHaircutVibPattern)
                        .setContentIntent(contentIntent)
                        .setSound(
                                Uri.parse(
                                        String.format(
                                                "android.resource://%s/%s", context.getPackageName(),
                                                R.raw.shave_and_a_haricut)))
                        .setLights(Color.YELLOW, 2000, 3000)
                        .setAutoCancel(true);

        return notificationBuilder.build();
    }
}
