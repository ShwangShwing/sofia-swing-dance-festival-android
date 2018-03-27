package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;

import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorDetailsInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setInstructor(InstructorModel instructor);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void stop();
        void setInstructorId(String id);
    }
}
