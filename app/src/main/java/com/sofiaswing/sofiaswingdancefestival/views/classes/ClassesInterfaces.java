package com.sofiaswing.sofiaswingdancefestival.views.classes;

import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;

import java.util.List;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class ClassesInterfaces {
    public interface IView {
        void setClassesTabs(List<ClassLevelModel> classLevels);
    }

    public interface IClassesLevelView {
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
