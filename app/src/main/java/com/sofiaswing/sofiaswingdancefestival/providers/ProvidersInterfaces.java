package com.sofiaswing.sofiaswingdancefestival.providers;

import android.arch.lifecycle.LiveData;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Pair;

import java.util.List;
import java.util.Map;

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
    }

    public interface ISettingsProvider {
        boolean isSubscribedForEvent(String eventId);
        void subscribeForEvent(String eventId, String eventName, long startTimestamp);
        void subscribeForEvent(String eventId, String eventName, long startTimestamp, long notifAdvanceTimeSeconds);
        void updateEventSubscription(String eventId, String eventName, long startTimestamp, boolean useDefaultNotifTime);
        void unsubscribeFromEvent(String eventId);

        long getEventsNotificationAdvanceTimeSeconds();
        void setEventsNotificationAdvanceTimeSeconds(long seconds);

        void setDefaultNotificationTimeToAllEvents();
        void setupAllNotificationAlarms();

        List<String> getSubscribedEventsIds();

        String getDefaultClassLevel();
        void setDefaultClassLevel(String classLevel);

        boolean areNewsNotificationsEnabled();
        void enableNewsNotifications();
        void disableNewsNotifications();

        boolean isYearFromDatabase();
        String getCurrentCustomSsdfYear();
        void setCurrentSsdfYearFromData();
        void setCurrentSsdfYear(String currentSsdfYear);
        LiveData<Pair<Boolean, String>> obsCurrentSsdfYear();
    }

    public interface ICurrentTimeProvider {
        long getCurrentTimeMs();
    }

    public interface IVolatileSettingsProvider {
        boolean isHackerModeEnabled();
        void enableHackerMode();
        void disableHackerMode();
        void setOverrideCurrentTime(boolean override, boolean freezeTime, long overridenTime);
        boolean isCurrentTimeOverriden();
        boolean isCurrentOverridenTimeFrozen();
        long getOverridenTimeMs();
        long getTimeOverridenAtMs();
    }

    public interface IPushNotificationsProvider {
        void subscribeForNews();
        void unsubscribeFromNews();
    }

    public interface IEventSubscriptionRefresher {
        void refreshEventSubscriptions();
    }

    public interface ISerializer {
        String serialize(Map<String, String> inMap);
        Map<String, String> deserializeToMap(String inString);
    }

    public interface IEventAlarmManager {
        void setNotificationAlarmForFutureEvent(
                String eventId,
                String eventName,
                long startTimestamp,
                long notifyTimestamp);
        void cancelNotificationAlarm(String eventId);
    }

    public interface INetworkImageLoader {
        Observable<Bitmap> getImage(String url);
    }
}
