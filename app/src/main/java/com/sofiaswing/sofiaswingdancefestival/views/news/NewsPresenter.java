package com.sofiaswing.sofiaswingdancefestival.views.news;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsPresenter implements NewsInterfaces.IPresenter {
    public final NewsInterfaces.IView view;
    private final DataInterfaces.INewsArticlesData newsArticlesData;
    private List<NewsArticleModel> newsArticles;

    private final CompositeDisposable subscriptions;

    public NewsPresenter(NewsInterfaces.IView view,
                         ProvidersInterfaces.IImageProvider imageProvider,
                         DataInterfaces.INewsArticlesData newsArticlesData) {
        this.view = view;
        this.newsArticlesData = newsArticlesData;
        view.setPresenter(this);
        view.setImageProvider(imageProvider);
        this.newsArticles = new ArrayList<>();
        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public NewsInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.subscriptions.add(
                this.newsArticlesData.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewsArticleModel>>() {
                    @Override
                    public void accept(List<NewsArticleModel> incomingNewsArticles) throws Exception {
                        newsArticles = incomingNewsArticles;
                        getView().setNews(newsArticles);
                    }
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
