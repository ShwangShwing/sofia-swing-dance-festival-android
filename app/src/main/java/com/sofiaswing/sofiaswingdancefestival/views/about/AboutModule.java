package com.sofiaswing.sofiaswingdancefestival.views.about;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/12/17.
 */

@Module
public class AboutModule {
    @Provides
    AboutInterfaces.IView provideAboutView() {
        return new AboutView();
    }

    @Provides
    AboutInterfaces.IPresenter providePresenter(AboutInterfaces.IView view) {
        return new AboutPresenter(view);
    }
}
