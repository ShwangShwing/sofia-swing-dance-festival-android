package com.sofiaswing.sofiaswingdancefestival.views.newsArticle;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

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
                                                             ProvidersInterfaces.IImageProvider imageProvider,
                                                             DataInterfaces.INewsArticlesData newsArticlesData)
    {
        return new NewsArticlePresenter(view, imageProvider, newsArticlesData);
    }
}
