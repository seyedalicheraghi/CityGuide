package org.smartcityguide.cityguide;
//https://github.com/codepath/android_guides/issues/220

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


public class GPSService extends Service {
    private static final String TAG = "MainActivity";
    private final IBinder gpsBinder = new LocationBinder();
    private LocationManager mLocationManager = null;
    Location newLocation = null;
//    Criteria criteria = new Criteria();
    private static final int LOCATION_INTERVAL = 100;
    private static final float LOCATION_DISTANCE = 5f;

    @Override
    public void onCreate() {
        super.onCreate();
        // Acquire a reference to the system Location Manager
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
//        createFineCriteria();
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.w(TAG, " Provider: " + location.getProvider() +
                        " Accuracy: " + location.getAccuracy() +
                        " Latitude: " + location.getLatitude() +
                        " Longitude: " + location.getLongitude() +
                        " Time: " + location.getTime());
                newLocation = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location Permission Error");
            return;
        }

//        mLocationManager.requestLocationUpdates(0,0,criteria, locationListener,null);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListener);
    }

    public Location gpsCoordinates() {
        return newLocation;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return gpsBinder;
    }


    public class LocationBinder extends Binder {
        GPSService getService() {
            return GPSService.this;
        }
    }

    /** this criteria needs high accuracy, high power, and cost */
//    public void createFineCriteria() {
//
//
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setSpeedRequired(false);
//        criteria.setCostAllowed(true);
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);
//        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
//
//
//    }

}