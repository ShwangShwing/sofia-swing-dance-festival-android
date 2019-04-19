package com.sofiaswing.sofiaswingdancefestival.ui;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/17/17.
 */

@Module
public class UiModule {
    @Provides
    UiInterfaces.IDrawerNavigationFactory provideDrawerNavigationFactory() {
        return new DrawerNavigationFactory();
    }

    @Provides
    UiInterfaces.IPopupCreator providePopupCreator(Context context) {
        return new ToastPopupCreator(context);
    }
}
