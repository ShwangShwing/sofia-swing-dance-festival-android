package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;


import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
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
                                ProvidersInterfaces.IImageProvider imageProvider,
                                DataInterfaces.INewsArticlesData newsArticlesData) {
        this.view = view;
        this.newsArticleData = newsArticlesData;
        this.view.setPresenter(this);
        this.view.setImageProvider(imageProvider);
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
                .subscribe(new Consumer<NewsArticleModel>() {
                    @Override
                    public void accept(NewsArticleModel newsArticle) throws Exception {
                        view.setNewsArticle(newsArticle);
                    }
                })
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
