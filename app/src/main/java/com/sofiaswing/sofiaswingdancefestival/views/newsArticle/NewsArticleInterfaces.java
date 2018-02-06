package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;

import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticleInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider);

        void setNewsArticle(NewsArticleModel newsArticle);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void stop();
        void setNewsArticleId(String id);
    }
}
