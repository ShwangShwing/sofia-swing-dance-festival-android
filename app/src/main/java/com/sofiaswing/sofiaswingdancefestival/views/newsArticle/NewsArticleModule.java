package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/9/17.
 */

@Module
public class NewsArticleModule {
    @Provides
    public NewsArticleInterfaces.IView provideView() {
        return new NewsArticleView();
    }

    @Provides
    public NewsArticleInterfaces.IPresenter providePresenter(NewsArticleInterfaces.IView view,
                                                             DataInterfaces.INewsArticlesData newsArticlesData)
    {
        return new NewsArticlePresenter(view, newsArticlesData);
    }
}
