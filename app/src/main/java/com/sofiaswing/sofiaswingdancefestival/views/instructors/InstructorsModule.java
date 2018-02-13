package com.sofiaswing.sofiaswingdancefestival.views.instructors;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/10/17.
 */

@Module
public class InstructorsModule {

    @Provides
    InstructorsInterfaces.IPresenter providePresenter(DataInterfaces.IInstructorsData instructorsData) {
        return new InstructorsPresenter(instructorsData);
    }
}
