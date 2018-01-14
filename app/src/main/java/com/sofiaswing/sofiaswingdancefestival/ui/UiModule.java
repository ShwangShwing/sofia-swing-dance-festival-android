package com.sofiaswing.sofiaswingdancefestival.ui;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/17/17.
 */

@Module
public class UiModule {
    @Provides
    UiInterfaces.ITitleFragmentFactory provideTitleFragmentFactory() {
        return new TitleFragmentFactory();
    }

    @Provides
    UiInterfaces.IDrawerNavigationFragmentFactory provideDrawerNavigationFragmentFactory() {
        return new DrawerNavigationFragmentFactory();
    }

    @Provides
    UiInterfaces.IPopupCreator providePopupCreator() {
        return new ToastPopupCreator();
    }
}
