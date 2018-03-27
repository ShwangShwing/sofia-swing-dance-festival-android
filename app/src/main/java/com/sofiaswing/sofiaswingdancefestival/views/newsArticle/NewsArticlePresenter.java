package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;


import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticlePresenter implements NewsArticleInterfaces.IPresenter{
    private final DataInterfaces.INewsArticlesData newsArticleData;
    private NewsArticleInterfaces.IView view;
    private String newsArticleId;

    private final CompositeDisposable subscriptions;

    public NewsArticlePresenter(NewsArticleInterfaces.IView view,
                                DataInterfaces.INewsArticlesData newsArticlesData) {
        this.view = view;
        this.newsArticleData = newsArticlesData;
        this.view.setPresenter(this);
        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public NewsArticleInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.subscriptions.add(
            this.newsArticleData.getById(newsArticleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsArticle -> view.setNewsArticle(newsArticle))
        );
    }

    @Override
    public void stop() {
        this.subscriptions.clear();
    }

    @Override
    public void setNewsArticleId(String id) {
        this.newsArticleId = id;
    }
}
