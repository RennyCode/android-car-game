package com.example.a23b_11345_l01b.Utilities;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.example.a23b_11345_l01b.Interfaces.StepCallback;

public class StepDetector {
    private float tiltAngle;
    private Sensor sensor;
    private SensorManager sensorManager;
    private StepCallback stepCallback;
    private  boolean ignore_step;
    private SensorEventListener sensorEventListener;
    public StepDetector(Context context, StepCallback stepCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallback = stepCallback;
        this.ignore_step = false;
        initEventListener();
    }
    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                tiltAngle = event.values[0];
                if(!ignore_step) {
                    if (tiltAngle < -2.5) {
                        stepCallback.tilted_to_lane(4);
                    } else if (tiltAngle > -2.5 && tiltAngle < -1) {
                        stepCallback.tilted_to_lane(3);
                    } else if (tiltAngle > -1 && tiltAngle < 1) {
                        stepCallback.tilted_to_lane(2);
                    } else if (tiltAngle > 1 && tiltAngle < 2.5) {
                        stepCallback.tilted_to_lane(1);
                    } else {
                        stepCallback.tilted_to_lane(0);
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
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