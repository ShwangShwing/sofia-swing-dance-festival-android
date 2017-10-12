package com.sofiaswing.sofiaswingdancefestival.views.settings;

import java.util.List;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class SettingsInterfaces {
    public interface IView {
        void setEventNotificationTimeSelection(long seconds);

        void setPresenter(IPresenter presenter);
    }

    public interface IPresenter {
        IView getView();
        void start();

        void setEventsNotificationAdvanceTimeSeconds(long seconds);
    }
}
