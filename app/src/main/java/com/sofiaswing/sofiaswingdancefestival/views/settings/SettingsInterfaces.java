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

        void notifyHackerModeEnabled();
        void showHackerModeEnabledIndicator();
        void hideHackerModeEnabledIndicator();

        void showHackerPanel();
        void hideHackerPanel();

        void setYearFromDatabase(boolean isSet);
        void setCustomYear(String customYear);
    }

    public interface IPresenter {
        IView getView();
        void start();

        void setEventsNotificationAdvanceTimeSeconds(long seconds);
        void enableHackerMode();
        void setYearFromDatabase();
        void setCustomYear(String customYear);
    }
}