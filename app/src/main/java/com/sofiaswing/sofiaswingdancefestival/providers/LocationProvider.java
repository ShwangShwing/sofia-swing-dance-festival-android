package com.sofiaswing.sofiaswingdancefestival.providers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class LocationProvider implements ProvidersInterfaces.ILocationProvider {
    // 0.3 m/s = 1.08 km/h
    private static final float ACCURACY_DECAY_MPS = 0.3f;
    private static final int GPS_AUTO_TURN_OFF_SECONDS = 20;
    private static final float GPS_DONT_TURN_OFF_DISTANCE_SPEED_MPS = 1f;

    private LocationManager locationManager;
    private Date locationTurnOffTime;
    private Date lastBestLocationFixTime;
    private Location lastBestLocation;
    private LocationListener locationListener;
    private List<ObservableEmitter<Location>> emitters;
    private SensorManager sensorManager;
    private Sensor significantMotionSensor;
    public TriggerEventListener triggerEventListener;

    public LocationProvider() {
        this.locationTurnOffTime = new Date(0);
        this.lastBestLocationFixTime = new Date(0);
        this.lastBestLocation = new Location("zero location");
        this.locationListener = null;
        this.emitters = new ArrayList<>();
    }

    @Override
    public void startLocationService(final Activity activity) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //don't start in this case
            return;
        }

        if (this.locationManager == null) {
            this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        }

        if (this.sensorManager == null) {
            this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        }

        if (this.significantMotionSensor == null) {
            this.significantMotionSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        }

        if (this.locationListener != null) {
            // Already started
            return;
        }

        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Date currentTime = new Date();
                long secondsSinceLastFix = currentTime.getTime() - lastBestLocationFixTime.getTime();

                float decayedAccuracySinceLastFix =
                        lastBestLocation.getAccuracy() + secondsSinceLastFix * ACCURACY_DECAY_MPS;
                if (location.getAccuracy() < decayedAccuracySinceLastFix) {
                    lastBestLocation = new Location(location);
                    lastBestLocationFixTime = currentTime;

                    for (ObservableEmitter<Location> emitter : emitters){
                        emitter.onNext(lastBestLocation);
                    }

                    if (location.getSpeed() >= GPS_DONT_TURN_OFF_DISTANCE_SPEED_MPS) {
                        locationTurnOffTime = new Date();
                        locationTurnOffTime.setTime(locationTurnOffTime.getTime()
                                + GPS_AUTO_TURN_OFF_SECONDS * 1000);
                    }

                    if (currentTime.getTime() >= locationTurnOffTime.getTime()) {
                        stopLocationService();

                        // Setup the sensor to auto turn on if significant motion is detected
                        triggerEventListener = new TriggerEventListener() {
                            @Override
                            public void onTrigger(TriggerEvent event) {
                                startLocationService(activity);
                            }
                        };

                        sensorManager.requestTriggerSensor(triggerEventListener, significantMotionSensor);
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }


        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener, Looper.getMainLooper());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener, Looper.getMainLooper());

        this.locationTurnOffTime = new Date();
        this.locationTurnOffTime.setTime(this.locationTurnOffTime.getTime()
                + GPS_AUTO_TURN_OFF_SECONDS * 1000);
    }

    @Override
    public void stopLocationService() {
        if (this.sensorManager != null) {
            this.sensorManager.cancelTriggerSensor(triggerEventListener, significantMotionSensor);
        }

        if (this.locationManager != null) {
            this.locationManager.removeUpdates(this.locationListener);
            this.locationListener = null;
        }
    }

    @Override
    public Observable<Location> getCurrentLocation() {
        Observable<Location> observable = Observable.create(new ObservableOnSubscribe<Location>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Location> e) throws Exception {
                emitters.add(e);
            }

        });

        return observable;
    }
}
