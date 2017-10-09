package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;


import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsArticleViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticlePresenter implements NewsArticleInterfaces.IPresenter{
    private final DataInterfaces.INewsArticlesData newsArticleData;
    private NewsArticleInterfaces.IView view;
    private String newsArticleId;

    public NewsArticlePresenter(NewsArticleInterfaces.IView view,
                                ProvidersInterfaces.IImageProvider imageProvider,
                                DataInterfaces.INewsArticlesData newsArticlesData) {
        this.view = view;
        this.newsArticleData = newsArticlesData;
        this.view.setPresenter(this);
        this.view.setImageProvider(imageProvider);
    }

    @Override
    public NewsArticleInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {
        this.newsArticleData.getById(newsArticleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewsArticleModel>() {
                    @Override
                    public void accept(NewsArticleModel newsArticle) throws Exception {
                        NewsArticleViewModel newsArticleView =
                                new NewsArticleViewModel(newsArticle.getPostedOn(),
                                        newsArticle.getImageUrl(),
                                        newsArticle.getText());
                        view.setNewsArticle(newsArticleView);
                    }
                });
    }

    @Override
    public void setNewsArticleId(String id) {
        this.newsArticleId = id;
    }
}
