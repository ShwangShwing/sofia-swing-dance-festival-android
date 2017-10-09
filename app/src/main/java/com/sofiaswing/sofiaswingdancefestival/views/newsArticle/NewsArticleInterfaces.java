package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;

import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.news.NewsArticleViewModel;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticleInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider);

        void setNewsArticle(NewsArticleViewModel newsArticle);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void setNewsArticleId(String id);
    }
}
