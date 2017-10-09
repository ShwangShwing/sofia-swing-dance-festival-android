package com.sofiaswing.sofiaswingdancefestival.providers;

import android.graphics.Bitmap;
import android.os.Looper;

import java.io.FileNotFoundException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class ImageProvider implements ProvidersInterfaces.IImageProvider {
    @Override
    public Observable<Bitmap> getImageFromUrl(final String url) {
        Observable<Bitmap> observable = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Bitmap> e) throws Exception {
                e.onError(new FileNotFoundException(String.format("Image with url of %s not found", url)));
            }
        });

        return observable;
    }
}
