package com.sofiaswing.sofiaswingdancefestival.views.schedule;

/**
 * Created by shwangshwing on 3/18/18.
 */

public class ScheduleInterfaces {
    public interface IView {

    }

    public interface IPresenter {
        void setView(IView view);
        void start();
        void stop();
    }
}
