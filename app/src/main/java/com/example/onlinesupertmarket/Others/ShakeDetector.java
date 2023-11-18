package com.example.onlinesupertmarket.Others;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {

    private static final int SHAKE_THRESHOLD = 14;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 1000;

    private SensorManager sensorManager;
    private OnShakeListener listener;
    private long lastShakeTime;
    private long lastUpdateTime;

    public ShakeDetector(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastUpdateTime;

        if (timeDifference > SHAKE_TIMEOUT) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
            if (acceleration > SHAKE_THRESHOLD) {
                lastUpdateTime = currentTime;
                if (currentTime - lastShakeTime > SHAKE_DURATION) {
                    lastShakeTime = currentTime;
                    if (listener != null) {
                        listener.onShake();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void unregisterListener() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    public void registerListener() {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }



    public interface OnShakeListener {
        void onShake();
    }
}
