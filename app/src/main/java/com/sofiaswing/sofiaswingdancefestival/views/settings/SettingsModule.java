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
    SettingsInterfaces.IPresenter providePresenter(
            ProvidersInterfaces.ISettingsProvider settingsProvider,
            ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider) {
        return new SettingsPresenter(settingsProvider, volatileSettingsProvider);
    }
}
