package org.smartcityguide.cityguide;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class CompassService extends Service implements SensorEventListener {

    private final IBinder compassBinder = new CompassService.DirectionBinder();
    int mAzimuth;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    boolean haveSensor = false, haveSensor2 = false;
    String where[] = {"North West","0"};
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private String TAG = "MainActivity";
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return compassBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                Log.e(TAG, "Your device doesn't support the Compass.");
            } else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        } else {
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
        where[1] = String.valueOf(mAzimuth);
        if (mAzimuth <= 23 || mAzimuth > 338)
            where[0] = "North";
        if (mAzimuth <= 338 && mAzimuth > 293)
            where[0] = "North West";
        if (mAzimuth <= 293 && mAzimuth > 248)
            where[0] = "West";
        if (mAzimuth <= 248 && mAzimuth > 203)
            where[0] = "South West";
        if (mAzimuth <= 203 && mAzimuth > 158)
            where[0] = "South";
        if (mAzimuth <= 158 && mAzimuth > 113)
            where[0] = "South East";
        if (mAzimuth <= 113 && mAzimuth > 68)
            where[0] = "East";
        if (mAzimuth <= 68 && mAzimuth > 23)
            where[0] = "North East";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public String[] currentDirection() {
        return where;
    }

    public class DirectionBinder extends Binder {
        CompassService getService() {
            return CompassService.this;
        }
    }
}
