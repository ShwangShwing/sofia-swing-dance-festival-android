package com.sofiaswing.sofiaswingdancefestival.views.classes;

import com.sofiaswing.sofiaswingdancefestival.models.ClassLevelModel;
import com.sofiaswing.sofiaswingdancefestival.models.ClassModel;

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

    public interface IPresenter {
        void setView(IView view);
        void start();
        void stop();

        Observable<List<ClassModel>> getClassesByLevel(String type);

        Observable<List<ClassModel>> getTasterClasses();

        void subscribeForEvent(String eventId, String eventName, int startTimestamp);

        void unsubscribeFromEvent(String eventId);

        boolean isSubscribedForEvent(String eventId);
    }
}
