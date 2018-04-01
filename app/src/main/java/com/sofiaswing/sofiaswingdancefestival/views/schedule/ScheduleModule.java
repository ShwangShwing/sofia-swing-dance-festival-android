package com.sofiaswing.sofiaswingdancefestival.views.schedule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 3/18/18.
 */

@Module
public class ScheduleModule {
    @Provides
    ScheduleInterfaces.IPresenter provideSchedulePresenter() {
        return new SchedulePresenter();
    }
}
