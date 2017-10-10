package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;

import com.sofiaswing.sofiaswingdancefestival.data.DataInterfaces;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

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
        DataInterfaces.IInstructorsData instructorsData,
        ProvidersInterfaces.IImageProvider imageProvider) {
        return new InstructorDetailsPresenter(view, instructorsData, imageProvider);
    }
}
