package com.example.a23b_11345_l01b.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;

import com.example.a23b_11345_l01b.Interfaces.StepCallback;

import java.util.logging.LogRecord;


public class StepDetector {

    private float tiltAngle;
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
//                float y = event.values[1];
//                float z = event.values[2];



//                tiltAngle = Math.atan2(x, Math.sqrt(y*y + z*z)) * 180/Math.PI;
                tiltAngle = x;

                System.out.println("tiltAngle = "+ tiltAngle);
                if (tiltAngle < -4) {
                    //to right
                    System.out.println("x above 30");
                    stepCallback.tilted_to_right();
                } else if (tiltAngle > 4) {
                    //to left
                    System.out.println("x below 30");

                    stepCallback.tilted_to_left();
                } else {
                    stepCallback.tilted_to_center();
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