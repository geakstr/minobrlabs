package ru.edu.asu.minobrlabs.sensors.local;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import java.util.Date;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Accel;
import ru.edu.asu.minobrlabs.db.entities.params.Gyro;
import ru.edu.asu.minobrlabs.db.entities.params.Light;
import ru.edu.asu.minobrlabs.sensors.AbstractSensorManager;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class LocalSensorsManager implements SensorEventListener {
    private ISensorCallback callback;

    private final SensorManager sensorManager;
    private final Sensor sensorLight;
    private final Sensor sensorGyro;
    private final Sensor sensorAccel;

    private final MicrophoneSensorManager microphoneSensorManager;

    // Store prev values for low pass filter
    private float[] accel = new float[3];
    private float[] gyro = new float[3];

    private long prevLightTime = new Date().getTime();
    private long prevAccelTime = new Date().getTime();
    private long prevGyroTime = new Date().getTime();

    public LocalSensorsManager() {
        sensorManager = (SensorManager) App.getInstance().getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        microphoneSensorManager = new MicrophoneSensorManager();
    }

    public void registerListeners() {
        if (null != sensorLight) {
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (null != sensorGyro) {
            sensorManager.registerListener(this, sensorGyro, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (null != sensorAccel) {
            sensorManager.registerListener(this, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterListeners() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(final SensorEvent event) {
        if (null == callback) {
            return;
        }

        final float[] vals = event.values;
        if (null == vals) {
            return;
        }
        SensorTypes type = null;
        final Bundle bundle = new Bundle();
        final long currentTime = new Date().getTime();
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                if (currentTime - prevLightTime >= 100) {
                    type = SensorTypes.LIGHT;
                    bundle.putSerializable(SensorCallback.bundleKey, new Light(vals));
                }
                break;
            case Sensor.TYPE_GYROSCOPE:
                if (currentTime - prevGyroTime >= 100) {
                    type = SensorTypes.GYRO;
                    gyro = AbstractSensorManager.lowPass(vals.clone(), gyro, 0.75f);
                    bundle.putSerializable(SensorCallback.bundleKey, new Gyro(gyro));
                }
                break;
            case Sensor.TYPE_ACCELEROMETER:
                if (currentTime - prevAccelTime >= 100) {
                    type = SensorTypes.ACCEL;
                    accel = AbstractSensorManager.lowPass(vals.clone(), accel, 0.5f);
                    bundle.putSerializable(SensorCallback.bundleKey, new Accel(accel));
                    prevAccelTime = currentTime;
                }
                break;
            default:
                break;
        }
        if (null == type) {
            return;
        }

        bundle.putSerializable(SensorCallback.bundleType, type);
        callback.onReceiveResult(Activity.RESULT_OK, bundle);
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

    }

    public void setCallback(final ISensorCallback callback) {
        this.callback = callback;
        microphoneSensorManager.setCallback(callback);
    }

    public void start() {
        microphoneSensorManager.start();
    }

    public void stop() {
        microphoneSensorManager.stop();
    }
}
