package com.sofiaswing.sofiaswingdancefestival.views.news;

import com.sofiaswing.sofiaswingdancefestival.models.NewsArticleModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.List;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider);

        void setNews(List<NewsArticleViewModel> newsArticles);
        void navigateToArticle(String articleId);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void selectNewsArticle(int index);
    }
}
