package com.sofiaswing.sofiaswingdancefestival.providers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class ProvidersInterfaces {
    public interface IImageProvider {
        Observable<Bitmap> getImageFromUrl(String url);
    }

    public interface ILocationProvider {
        void startLocationService(Activity activity);

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
    }

    public interface IVolatileSettingsProvider {
        boolean isHackerModeEnabled();
        void enableHackerMode();
        void disableHackerMode();
        boolean isYearFromDatabase();
        String getCurrentCustomSsdfYear();
        void setCurrentSsdfYearFromData();
        void setCurrentSsdfYear(String currentSsdfYear);
    }
}