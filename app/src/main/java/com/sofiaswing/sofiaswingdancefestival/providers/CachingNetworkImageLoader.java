package com.sofiaswing.sofiaswingdancefestival.providers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import io.reactivex.Observable;


public class CachingNetworkImageLoader implements ProvidersInterfaces.INetworkImageLoader {
    private final int DOWNLOAD_CACHED_IMAGE_AFTER_SECONDS = 3666;
    private final Context ctx;
    private final ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider;

    public CachingNetworkImageLoader(Context ctx, ProvidersInterfaces.ICurrentTimeProvider currentTimeProvider) {
        this.ctx = ctx;
        this.currentTimeProvider = currentTimeProvider;
    }

    public Observable<Bitmap> getImage(String strUrl) {
        return Observable.create(e -> {
            boolean downloadPicture = true;

            String cacheFilename = strUrl.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
            // load the cached file if it exists
            try {
                File inFile = new File(ctx.getCacheDir(), cacheFilename);
                FileInputStream inFileStr = new FileInputStream(inFile);
                Bitmap cacheBmp = BitmapFactory.decodeStream(inFileStr);
                if (cacheBmp != null) {
                    e.onNext(cacheBmp);
                    long currentTimeMs = this.currentTimeProvider.getCurrentTimeMs();
                    long fileTimeMs = inFile.lastModified();
                    if (currentTimeMs - fileTimeMs < DOWNLOAD_CACHED_IMAGE_AFTER_SECONDS * 1000) {
                        // don't download the picture if it has been retrieved a short while ago
                        downloadPicture = false;
                    }
                }
                inFileStr.close();
            } catch (IOException ex) {
                // Errors in reading the file should not crash the app.
            }

            // download the picture and cache it
            if (downloadPicture) {
                try {
                    URL url = new URL(strUrl);
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(true);
                    Bitmap bmp = BitmapFactory.decodeStream(connection.getInputStream());
                    if (bmp != null) {
                        e.onNext(bmp);
                        try {
                            File outFile = new File(ctx.getCacheDir(), cacheFilename);
                            FileOutputStream outFileStr = new FileOutputStream(outFile, false);
                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outFileStr);
                            outFileStr.close();
                        } catch (IOException ex) {

                        }
                    }
                } catch (IOException ex) {
                    // Errors in the network not crash the app.
                }
            }

            e.onComplete();
        });
    }
}
