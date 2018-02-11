package com.sofiaswing.sofiaswingdancefestival.providers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;

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
                ImageGetter imageGetter = new ImageGetter(new IImageReadyCallback() {
                    @Override
                    public void onImageReady(Bitmap bitmap) {
                        if (!e.isDisposed()) {
                            if (bitmap != null) {
                                e.onNext(bitmap);
                            } else {
                                e.onError(new FileNotFoundException(String.format("Image with url of %s not found", url)));
                            }
                        }
                    }
                });

                imageGetter.execute(url);
            }
        });

        return observable;
    }

    private class ImageGetter extends AsyncTask<String, Object, Bitmap> {
        private final IImageReadyCallback imageReadyCallback;

        public ImageGetter(IImageReadyCallback imageReadyCallback) {
            this.imageReadyCallback = imageReadyCallback;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String requestUrl = params[0];

            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                return bitmap;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            this.imageReadyCallback.onImageReady(bitmap);
        }


    }

    private interface IImageReadyCallback {
        void onImageReady(Bitmap bitmap);
    }
}
