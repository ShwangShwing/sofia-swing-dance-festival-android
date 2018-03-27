package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;

import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticleInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setNewsArticle(NewsArticleModel newsArticle);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void stop();
        void setNewsArticleId(String id);
    }
}
