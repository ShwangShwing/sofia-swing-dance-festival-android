package com.sofiaswing.sofiaswingdancefestival.views.settings;

import com.sofiaswing.sofiaswingdancefestival.ui.UiInterfaces;

import java.util.List;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class SettingsInterfaces {
    public interface IView {
        void setEventNotificationTimeSelection(long seconds);

        void notifyHackerModeEnabled();
        void showHackerModeEnabledIndicator();
        void hideHackerModeEnabledIndicator();

        void showHackerPanel();
        void hideHackerPanel();

        void setYearFromDatabase(boolean isSet);
        void setCustomYear(String customYear);
        void setSsdfYears(List<String> ssdfYears);
        void notifyCurrentTimeMs(long currentTimeMs);
        void setNotifyOverrideTimeState(boolean isOverriden, boolean isFrozen);
    }

    public interface IPresenter {
        void setView(IView view);
        void start();
        void stop();

        void setEventsNotificationAdvanceTimeSeconds(long seconds);
        void enableHackerMode();
        void setYearFromDatabase();
        void setCustomYear(String customYear);

        void createTestNotification(String id, String name, long startTime, long notifyTime);
        void notifyCurrentTime();
        void setTimeOverride(boolean override, boolean freezeOverridenTime, long overridenTime);
    }
}
