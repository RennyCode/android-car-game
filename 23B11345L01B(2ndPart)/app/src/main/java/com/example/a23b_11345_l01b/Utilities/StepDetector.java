package com.example.a23b_11345_l01b.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.a23b_11345_l01b.Interfaces.StepCallback;


public class StepDetector {
    private double tiltAngle;
    private Sensor sensor;
    private SensorManager sensorManager;
    private StepCallback stepCallback;

    private int stepCounterX = 0;
    private int stepCounterY = 0;
    private long timestamp = 0;

    private SensorEventListener sensorEventListener;

    public StepDetector(Context context, StepCallback stepCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallback = stepCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                tiltAngle = Math.atan2(x, Math.sqrt(y*y + z*z)) * 180/Math.PI;
                if (tiltAngle> 30) {
                    //to right
                    stepCallback.tilted_to_right();
                } else if (tiltAngle < -30) {
                    //to left
                    stepCallback.tilted_to_left();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }



    public int getStepsY() {
        return this.stepCounterY;
    }

    public int getStepsX() {
        return this.stepCounterX;
    }
    public double getTiltAngle() {
        return this.tiltAngle;
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }

}