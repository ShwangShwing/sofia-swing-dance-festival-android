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
        String getCurrentSsdfYear();
    }
}
