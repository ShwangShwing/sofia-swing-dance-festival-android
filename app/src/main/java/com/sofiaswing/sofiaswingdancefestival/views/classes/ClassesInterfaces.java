package com.sofiaswing.sofiaswingdancefestival.views.classes;

import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;
import com.sofiaswing.sofiaswingdancefestival.models.InstructorModel;
import com.sofiaswing.sofiaswingdancefestival.models.VenueModel;

import java.io.Serializable;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class ClassesInterfaces {
    public interface IView {
        void setClassesTabs(List<ClassLevelModel> classLevels);
    }

    public interface IClassesLevelView {
        boolean isTaster();
        String getClassLevel();
        void setClasses(List<ClassPresenterModel> classes);
    }

    public interface IPresenter {
        void setView(IView view);
        void start();
        void stop();
    }

    public interface IClassesLevelPresenter {
        void setView(IClassesLevelView view);
        void start();
        void stop();
        void subscribeForEvent(String eventId, String eventName, int startTimestamp);
        void unsubscribeFromEvent(String eventId);
    }
}
