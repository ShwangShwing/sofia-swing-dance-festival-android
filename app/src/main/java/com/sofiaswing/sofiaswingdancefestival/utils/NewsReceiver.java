package com.sofiaswing.sofiaswingdancefestival.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sofiaswing.sofiaswingdancefestival.R;

import com.sofiaswing.sofiaswingdancefestival.views.main.MainActivity;

public class NewsReceiver extends BroadcastReceiver {
    public static final String NAVIGATE_TO_NEWS_KEY = "navigate_to_key";
    public static String INTENT_ACTION_NEWS = "ACTION_INTENT_NEWS";
    public static String NEWS_ARTICLE_CONTENT_KEY = "news_article_content_key";
    private static String NEWS_CHANNEL_NAME = "news";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != INTENT_ACTION_NEWS) return;

        String newsArticleContents = intent.getStringExtra(NEWS_ARTICLE_CONTENT_KEY);
        if (newsArticleContents == null) newsArticleContents = "";



        Intent resultIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        resultIntent.putExtra(NAVIGATE_TO_NEWS_KEY, true);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, newsArticleContents.hashCode(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = NotificationCreator.getNotification(
                context, NEWS_CHANNEL_NAME,
                context.getString(R.string.news),
                newsArticleContents, pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(newsArticleContents.hashCode(), notification);
    }
}
