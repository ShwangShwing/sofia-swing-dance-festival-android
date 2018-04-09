package com.sofiaswing.sofiaswingdancefestival.providers;

import android.location.Location;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class ProvidersInterfaces {

    public interface ILocationProvider {
        void startLocationService();

        void stopLocationService();

        Observable<Location> getCurrentLocation();
    }

    public interface ICurrentSsdfYearProvider {
        Observable<String> getCurrentSsdfYear();
        void setCurrentSsdfYearFromData();
        void setCurrentSsdfYear(String currentSsdfYear);
    }

    public interface ISettingsProvider {
        boolean isSubscribedForEvent(String eventId);
        void subscribeForEvent(String eventId, String eventName, long startTimestamp, long notifyTimestamp);
        void unsubscribeFromEvent(String eventId);

        long getEventsNotificationAdvanceTimeSeconds();
        void setEventsNotificationAdvanceTimeSeconds(long seconds);

        void setupAllNotificationAlarms();

        List<String> getSubscribedEventsIds();
    }

    public interface ICurrentTimeProvider {
        long getCurrentTimeMs();
    }

    public interface IVolatileSettingsProvider {
        boolean isHackerModeEnabled();
        void enableHackerMode();
        void disableHackerMode();
        boolean isYearFromDatabase();
        String getCurrentCustomSsdfYear();
        void setCurrentSsdfYearFromData();
        void setCurrentSsdfYear(String currentSsdfYear);
        void setOverrideCurrentTime(boolean override, boolean freezeTime, long overridenTime);
        boolean isCurrentTimeOverriden();
        boolean isCurrentOverridenTimeFrozen();
        long getOverridenTimeMs();
        long getTimeOverridenAtMs();
    }
}
