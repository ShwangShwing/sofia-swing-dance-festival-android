package com.sofiaswing.sofiaswingdancefestival.views.news;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/9/17.
 */

@Module
public class NewsModule {
    @Provides
    NewsInterfaces.IView provideNewsView() {
        return new NewsView();
    }

    @Provides
    NewsInterfaces.IPresenter provideNewsPresenter(
            NewsInterfaces.IView view,
            ProvidersInterfaces.IImageProvider imageProvider,
            DataInterfaces.INewsArticlesData newsArticlesData) {
        return new NewsPresenter(view, imageProvider, newsArticlesData);
    }
}
