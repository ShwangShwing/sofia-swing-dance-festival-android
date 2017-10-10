package com.sofiaswing.sofiaswingdancefestival.views.instructorDetails;

import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;
import com.sofiaswing.sofiaswingdancefestival.views.instructors.InstructorViewModel;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorDetailsInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider);

        void setInstructor(InstructorViewModel instructor);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void setInstructorId(String id);
    }
}
