package com.sofiaswing.sofiaswingdancefestival.data.LocalData;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/9/17.
 */

@Module
public class LocalMockDataModule {
    @Provides
    DataInterfaces.INewsArticlesData provideNewsArticlesMockData() {
        return new NewsArticlesLocalMockData();
    }
}
