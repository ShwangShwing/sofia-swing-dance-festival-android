package com.sofiaswing.sofiaswingdancefestival.utils;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String articleText = remoteMessage.getData().get("articleText");
        if (articleText == null) articleText = "";
        String dbCollection = remoteMessage.getData().get("dbCollection");
        if (dbCollection == null) dbCollection = "";
        String newsArticleId = remoteMessage.getData().get("newsArticleId");
        if (newsArticleId == null) newsArticleId = "";
        Intent newIntent = new Intent(this, NewsReceiver.class);
        newIntent.setAction(NewsReceiver.INTENT_ACTION_NEWS);
        newIntent.putExtra(NewsReceiver.NEWS_ARTICLE_CONTENT_KEY, articleText);
        newIntent.putExtra(NewsReceiver.NEWS_ARTICLE_DB_COLLECTION_KEY, dbCollection);
        newIntent.putExtra(NewsReceiver.NEWS_ARTICLE_ID_KEY, newsArticleId);

        sendBroadcast(newIntent);
    }
}
