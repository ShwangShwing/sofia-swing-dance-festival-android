package com.sofiaswing.sofiaswingdancefestival.views.settings;

import com.sofiaswing.sofiaswingdancefestival.ui.UiInterfaces;

import java.util.List;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class SettingsInterfaces {
    public interface IView {
        void setEventNotificationTimeSelection(long seconds);

        void setPresenter(IPresenter presenter);

        void setPopupCreator(UiInterfaces.IPopupCreator popupCreator);
    }

    public interface IPresenter {
        IView getView();
        void start();

        void setEventsNotificationAdvanceTimeSeconds(long seconds);
    }
}
