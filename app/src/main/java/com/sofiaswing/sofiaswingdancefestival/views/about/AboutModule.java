package com.sofiaswing.sofiaswingdancefestival.views.about;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/12/17.
 */

@Module
public class AboutModule {

    @Provides
    AboutInterfaces.IPresenter providePresenter() {
        return new AboutPresenter();
    }
}
