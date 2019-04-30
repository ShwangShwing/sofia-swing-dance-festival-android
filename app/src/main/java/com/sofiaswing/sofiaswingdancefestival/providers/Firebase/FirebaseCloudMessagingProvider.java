package com.sofiaswing.sofiaswingdancefestival.providers.Firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

public class FirebaseCloudMessagingProvider implements ProvidersInterfaces.IPushNotificationsProvider {
    private static final String NEWS_TOPIC = "ssdf-news";

    @Override
    public void subscribeForNews() {
        FirebaseMessaging.getInstance().subscribeToTopic(NEWS_TOPIC);
    }

    @Override
    public void unsubscribeFromNews() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(NEWS_TOPIC);
    }
}
