package com.sofiaswing.sofiaswingdancefestival.views.instructors;

import com.sofiaswing.sofiaswingdancefestival.providers.ProvidersInterfaces;

import java.util.List;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorsInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
        void setImageProvider(ProvidersInterfaces.IImageProvider imageProvider);

        void setInstructors(List<InstructorViewModel> newsArticles);
        void navigateToInstructor(String instructorId);
    }

    public interface IPresenter {
        IView getView();
        void start();
        void selectInstructor(int index);
    }
}
