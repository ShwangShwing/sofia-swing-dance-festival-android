package com.sofiaswing.sofiaswingdancefestival.views.settings;

import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.ui.UiInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/12/17.
 */

@Module
public class SettingsModule {
    @Provides
    SettingsInterfaces.IView provideAboutView() {
        return new SettingsView();
    }

    @Provides
    SettingsInterfaces.IPresenter providePresenter(
            SettingsInterfaces.IView view,
            ProvidersInterfaces.ISettingsProvider settingsProvider,
            UiInterfaces.IPopupCreator popupCreator) {
        return new SettingsPresenter(view, settingsProvider, popupCreator);
    }
}
