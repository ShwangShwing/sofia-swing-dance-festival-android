package com.sofiaswing.sofiaswingdancefestival.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sofiaswing.sofiaswingdancefestival.BuildConfig;
import com.sofiaswing.sofiaswingdancefestival.R;
import com.sofiaswing.sofiaswingdancefestival.SofiaSwingDanceFestivalApplication;
import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.main.MainActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsReceiver extends BroadcastReceiver {
    public static final String NAVIGATE_TO_NEWS_KEY = "navigate_to_news_key";
    public static String INTENT_ACTION_NEWS = "ACTION_INTENT_NEWS";
    public static String NEWS_ARTICLE_CONTENT_KEY = "news_article_content_key";
    public static String NEWS_ARTICLE_DB_COLLECTION_KEY = "news_article_db_collection_key";
    public static String NEWS_ARTICLE_ID_KEY = "news_article_id_key";
    private static String NEWS_CHANNEL_NAME = "news";

    @Inject
    ProvidersInterfaces.ICurrentSsdfYearProvider currentSsdfYearProvider;
    @Inject
    DataInterfaces.INewsArticlesData newsArticlesData;
    @Inject
    DataInterfaces.IBrokenDbConnectionFixer brokenDbConnectionFixer;

    @SuppressLint("CheckResult")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != INTENT_ACTION_NEWS) return;

        this.inject(context);

        String newsArticleContents = intent.getStringExtra(NEWS_ARTICLE_CONTENT_KEY);
        String dbCollection = intent.getStringExtra(NEWS_ARTICLE_DB_COLLECTION_KEY);

        if (newsArticleContents == null || dbCollection == null) {
            // TODO: report an error
            return;
        }

        final PendingResult pendingResult = goAsync();

        this.currentSsdfYearProvider
                .getCurrentSsdfYear()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .first("nothing_emmited")
                .subscribe(currentSsdfYear -> {
                    if (currentSsdfYear.equals("nothing_emmited")) {
                        // TODO: report an error
                    }

                    if (currentSsdfYear.equals(dbCollection)) {
                        Intent resultIntent = new Intent(context.getApplicationContext(), MainActivity.class);
                        resultIntent.putExtra(NAVIGATE_TO_NEWS_KEY, true);
                        PendingIntent pendingIntent =
                                PendingIntent.getActivity(context, newsArticleContents.hashCode(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        Notification notification = NotificationCreator.getNotification(
                                context, 
                                NEWS_CHANNEL_NAME,
                                context.getString(R.string.channel_name_news),
                                context.getString(R.string.news),
                                newsArticleContents, pendingIntent);

                        NotificationManager notificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        notificationManager.notify(newsArticleContents.hashCode(), notification);

                        String newsArticleId = intent.getStringExtra(NEWS_ARTICLE_ID_KEY);
                        if (newsArticleId == null && !BuildConfig.DEBUG) {
                            newsArticleId = "";
                        }

                        this.brokenDbConnectionFixer.fixBrokenDbConnection();
                        // Make sure that the news article is actually downloaded
                        this.newsArticlesData.getById(newsArticleId).firstOrError().subscribe();
                    }

                    pendingResult.finish();
                });
    }

    private void inject(Context context) {
        SofiaSwingDanceFestivalApplication app = (SofiaSwingDanceFestivalApplication) context.getApplicationContext();
        app.getComponent().inject(this);
    }
}
