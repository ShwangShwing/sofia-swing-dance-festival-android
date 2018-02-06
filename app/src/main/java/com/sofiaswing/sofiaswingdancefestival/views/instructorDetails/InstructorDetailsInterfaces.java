package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;

import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorDetailsInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider);

        void setInstructor(InstructorModel instructor);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void stop();
        void setInstructorId(String id);
    }
}
