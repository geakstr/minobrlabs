package ru.edu.asu.minobrlabs.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

public class LocalSensorsManager implements SensorEventListener {
    private static final String TAG = LocalSensorsManager.class.getSimpleName();

    private ISensorCallback callback;

    private SensorManager sensorManager;
    private Sensor sensorLight;
    private Sensor sensorGyro;
    private Sensor sensorAccel;

    private AudioRecordThread audioRecordThread;

    public LocalSensorsManager(final Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        this.sensorGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        this.sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.audioRecordThread = new AudioRecordThread();
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

    public void start(final ISensorCallback callback) {
        this.callback = callback;

        this.audioRecordThread.setCallback(callback);
        this.audioRecordThread.setRunning(true);
        this.audioRecordThread.start();
    }

    public void stop() {
        this.audioRecordThread.setRunning(false);
    }
}
