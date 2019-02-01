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
    NewsInterfaces.IPresenter provideNewsPresenter(
            DataInterfaces.INewsArticlesData newsArticlesData,
            ProvidersInterfaces.IHackerSettingsProvider volatileSettingsProvider,
            DataInterfaces.IBrokenDbConnectionFixer brokenDbConnectionFixer) {
        return new NewsPresenter(newsArticlesData, volatileSettingsProvider, brokenDbConnectionFixer);
    }
}
