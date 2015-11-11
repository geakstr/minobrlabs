package ru.edu.asu.minobrlabs.sensors.local;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import ru.edu.asu.minobrlabs.Application;
import ru.edu.asu.minobrlabs.db.entities.Stat;
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
    private float[] accel;
    private float[] gyro;

    public LocalSensorsManager() {
        sensorManager = (SensorManager) Application.getInstance().getSystemService(Context.SENSOR_SERVICE);
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

        int type = 0;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                type = SensorTypes.LIGHT;
                break;
            case Sensor.TYPE_GYROSCOPE:
                type = SensorTypes.GYRO;
                break;
            case Sensor.TYPE_ACCELEROMETER:
                type = SensorTypes.ACCEL;
                break;
            default:
                break;
        }
        if (0 == type) {
            return;
        }

        final float[] vals = event.values;
        if (null == vals) {
            return;
        }

        if (type == SensorTypes.ACCEL) {
            accel = AbstractSensorManager.lowPass(vals.clone(), accel, 0.5f);
            System.arraycopy(accel, 0, vals, 0, accel.length);
        } else if (type == SensorTypes.GYRO) {
            gyro = AbstractSensorManager.lowPass(vals.clone(), gyro, 0.75f);
            System.arraycopy(gyro, 0, vals, 0, gyro.length);
        }

        final Bundle bundle = new Bundle();
        bundle.putSerializable(SensorCallback.bundleKey, new Stat(type, vals));

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
