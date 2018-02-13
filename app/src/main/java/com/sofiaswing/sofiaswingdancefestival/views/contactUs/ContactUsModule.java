package com.sofiaswing.sofiaswingdancefestival.views.contactUs;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/12/17.
 */

@Module
public class ContactUsModule {

    @Provides
    ContactUsInterfaces.IPresenter providePresenter() {
        return new ContactUsPresenter();
    }
}
