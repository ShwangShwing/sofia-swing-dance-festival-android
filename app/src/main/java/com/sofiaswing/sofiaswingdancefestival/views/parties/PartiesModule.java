package com.sofiaswing.sofiaswingdancefestival.views.parties;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/11/17.
 */

@Module
public class PartiesModule {

    @Provides
    PartiesInterfaces.IPresenter provideNewsPresenter(
            DataInterfaces.IPartiesData partiesData,
            ProvidersInterfaces.ISettingsProvider settingsProvider) {
        return new PartiesPresenter(partiesData, settingsProvider);
    }
}
