package com.sofiaswing.sofiaswingdancefestival.views.settings;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

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
            ProvidersInterfaces.IVolatileSettingsProvider volatileSettingsProvider,
            DataInterfaces.ISsdfYearsData ssdfYearsData) {
        return new SettingsPresenter(settingsProvider, volatileSettingsProvider, ssdfYearsData);
    }
}
