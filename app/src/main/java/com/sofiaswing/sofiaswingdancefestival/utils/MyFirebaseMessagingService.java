package com.sofiaswing.sofiaswingdancefestival.utils;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String articleText = remoteMessage.getData().get("articleText");
        if (articleText == null) articleText = "";
        Intent newIntent = new Intent();
        newIntent.setAction(NewsReceiver.INTENT_ACTION_NEWS);
        newIntent.putExtra(NewsReceiver.NEWS_ARTICLE_CONTENT_KEY, articleText);

        sendBroadcast(newIntent);
    }
}
