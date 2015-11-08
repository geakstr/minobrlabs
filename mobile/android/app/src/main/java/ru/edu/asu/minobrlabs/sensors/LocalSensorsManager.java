package ru.edu.asu.minobrlabs.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class LocalSensorsManager implements SensorEventListener {
    private WebViewSensorCallback callback;

    private SensorManager localSensorManager;
    private Sensor localSensorLight;
    private Sensor localSensorGyro;
    private Sensor localSensorAccel;

    public LocalSensorsManager(final Context context) {
        this.localSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.localSensorLight = localSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        this.localSensorGyro = localSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        this.localSensorAccel = localSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void registerListeners() {
        if (null != localSensorLight) {
            localSensorManager.registerListener(this, localSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (null != localSensorGyro) {
            localSensorManager.registerListener(this, localSensorGyro, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (null != localSensorAccel) {
            localSensorManager.registerListener(this, localSensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterListeners() {
        localSensorManager.unregisterListener(this);
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
        final float[] vals = event.values;

        if (0 == type || null == vals) {
            return;
        }

        final Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putFloatArray("values", vals);
        callback.onReceiveResult(Activity.RESULT_OK, bundle);
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

    }

    public void start(final WebViewSensorCallback callback) {
        this.callback = callback;
    }
}
