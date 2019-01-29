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

import io.reactivex.Observable;


public class CachingNetworkImageLoader implements ProvidersInterfaces.INetworkImageLoader {
    private final Context ctx;

    public CachingNetworkImageLoader(Context ctx) {
        this.ctx = ctx;
    }

    public Observable<Bitmap> getImage(String strUrl) {
        return Observable.create(e -> {
            try {
                String cacheFilename = strUrl.replaceAll("[^a-zA-Z0-9-_\\.]", "_");

                try {
                    File inFile = new File(ctx.getCacheDir(), cacheFilename);
                    FileInputStream inFileStr = new FileInputStream(inFile);
                    Bitmap cacheBmp = BitmapFactory.decodeStream(inFileStr);
                    if (cacheBmp != null) {
                        e.onNext(cacheBmp);
                    }
                    inFileStr.close();
                }
                catch (IOException ex) {

                }

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
                    }
                    catch (IOException ex) {

                    }
                }

            } catch (IOException ex) {

            }

            e.onComplete();
        });
    }
}
