package com.sofiaswing.sofiaswingdancefestival.data.LocalData;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticlesLocalMockData implements DataInterfaces.INewsArticlesData {
    private List<NewsArticleModel> newsArticles;

    @Inject
    public NewsArticlesLocalMockData() {
        this.newsArticles = new ArrayList<>();
        this.newsArticles.add(new NewsArticleModel("1", new Date(), "", "Lorem ipsum 1"));
        this.newsArticles.add(new NewsArticleModel("1", new Date(), "", "Lorem ipsum 2"));
        this.newsArticles.add(new NewsArticleModel("1", new Date(), "", "Lorem ipsum 3"));
        this.newsArticles.add(new NewsArticleModel("1", new Date(), "", "Lorem ipsum 4"));
        this.newsArticles.add(new NewsArticleModel("1", new Date(), "", "Lorem ipsum 5"));
        this.newsArticles.add(new NewsArticleModel("1", new Date(), "", "Lorem ipsum 6"));
        this.newsArticles.add(new NewsArticleModel("1", new Date(), "", "Lorem ipsum 7"));
    }

    @Override
    public Observable<List<NewsArticleModel>> getAll() {
        return Observable.just(this.newsArticles);
    }

    @Override
    public Observable<NewsArticleModel> getById(String id) {
        for (NewsArticleModel article : this.newsArticles) {
            if (article.getId().equals(id)) {
                return Observable.just(article);
            }
        }

        return Observable.error(new InvalidParameterException(String.format("News article with id of %s not found", id)));
    }
}
