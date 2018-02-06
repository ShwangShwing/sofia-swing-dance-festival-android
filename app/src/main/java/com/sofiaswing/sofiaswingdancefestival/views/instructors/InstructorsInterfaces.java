package com.sofiaswing.sofiaswingdancefestival.views.instructors;

import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.List;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorsInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider);

        void setInstructors(List<InstructorModel> newsArticles);
        void navigateToInstructor(String instructorId);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void stop();
        void selectInstructor(int index);
    }
}