package com.sofiaswing.sofiaswingdancefestival.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.sofiaswing.sofiaswingdancefestival.R;

public class NotificationCreator {
    public static Notification getNotification(
            Context context,
            String channelId,
            String channelName,
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

        final Uri soundUri = Uri.parse(
                String.format(
                        "android.resource://%s/%s", context.getPackageName(),
                        R.raw.shave_and_a_haricut));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.YELLOW);
            channel.enableVibration(true);
            channel.setVibrationPattern(shaveAndHaircutVibPattern);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(soundUri, attributes);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.app_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo))
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setVibrate(shaveAndHaircutVibPattern)
                        .setContentIntent(contentIntent)
                        .setSound(soundUri)
                        .setLights(Color.YELLOW, 2000, 3000)
                        .setAutoCancel(true);

        return notificationBuilder.build();
    }
}
