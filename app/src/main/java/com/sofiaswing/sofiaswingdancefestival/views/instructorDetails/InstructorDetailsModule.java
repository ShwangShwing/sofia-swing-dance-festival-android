package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shwangshwing on 10/10/17.
 */

@Module
public class InstructorDetailsModule {
    @Provides
    InstructorDetailsInterfaces.IView provideView() {
        return new InstructorDetailsView();
    }

    @Provides
    InstructorDetailsInterfaces.IPresenter providePresenter(
        InstructorDetailsInterfaces.IView view,
        DataInterfaces.IInstructorsData instructorsData) {
        return new InstructorDetailsPresenter(view, instructorsData);
    }
}
