package com.sofiaswing.sofiaswingdancefestival.providers;

import android.graphics.Bitmap;

import io.reactivex.Observable;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class ProvidersInterfaces {
    public interface IImageProvider {
        Observable<Bitmap> getImageFromUrl(String url);
    }

    public interface ICurrentSsdfYearProvider {
        String getCurrentSsdfYear();
    }
}
