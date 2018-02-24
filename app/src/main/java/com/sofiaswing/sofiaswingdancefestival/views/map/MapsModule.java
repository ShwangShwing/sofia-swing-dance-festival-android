package com.sofiaswing.sofiaswingdancefestival.views.map;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ivo on 16.02.18.
 */

@Module
public class MapsModule {
  @Provides
  MapsInterfaces.IPresenter providePresenter(DataInterfaces.IVenuesData venuesData) {
    return new MapsPresenter(venuesData);
  }
}
