package com.sofiaswing.sofiaswingdancefestival.views.news;

import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;

import java.util.List;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsInterfaces {
    public interface IView {
        void setNews(List<NewsArticleModel> newsArticles);
        void navigateToArticle(String articleId);
    }

    public interface IPresenter {
        void setView(IView view);
        void start();
        void stop();
        void selectNewsArticle(int index);
    }
}
