package com.sofiaswing.sofiaswingdancefestival.views.contactUs;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/12/17.
 */

@Module
public class ContactUsModule {
    @Provides
    ContactUsInterfaces.IView provideAboutView() {
        return new ContactUsView();
    }

    @Provides
    ContactUsInterfaces.IPresenter providePresenter(ContactUsInterfaces.IView view) {
        return new ContactUsPresenter(view);
    }
}
