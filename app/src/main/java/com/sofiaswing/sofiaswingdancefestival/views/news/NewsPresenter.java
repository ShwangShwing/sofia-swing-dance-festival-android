package com.sofiaswing.sofiaswingdancefestival.views.news;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsPresenter implements NewsInterfaces.IPresenter {
    public NewsInterfaces.IView view;
    private final DataInterfaces.INewsArticlesData newsArticlesData;
    private final ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider;
    private List<NewsArticleModel> newsArticles;

    private final CompositeDisposable subscriptions;

    public NewsPresenter(DataInterfaces.INewsArticlesData newsArticlesData,
                         ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider) {
        this.newsArticlesData = newsArticlesData;
        this.volatileSettingsProvider = volatileSettingsProvider;
        this.newsArticles = new ArrayList<>();
        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public void setView(NewsInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {
        this.subscriptions.add(
                this.newsArticlesData.getAll(volatileSettingsProvider.isHackerModeEnabled())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(incomingNewsArticles -> {
                    newsArticles = incomingNewsArticles;
                    view.setNews(newsArticles);
                })
        );
    }

    @Override
    public void stop() {
        this.subscriptions.clear();
    }

    @Override
    public void selectNewsArticle(int index) {
        String id = newsArticles.get(index).getId();
        this.view.navigateToArticle(id);
    }
}
