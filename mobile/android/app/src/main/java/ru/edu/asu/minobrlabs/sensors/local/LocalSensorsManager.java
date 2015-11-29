package ru.edu.asu.minobrlabs.sensors.local;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.Date;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Accel;
import ru.edu.asu.minobrlabs.db.entities.params.Gyro;
import ru.edu.asu.minobrlabs.db.entities.params.Light;
import ru.edu.asu.minobrlabs.sensors.AbstractSensorManager;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

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

    private long lightSleepTime;
    private long accelSleepTime;
    private long gyroSleepTime;

    private long prevLightTime = new Date().getTime();
    private long prevAccelTime = new Date().getTime();
    private long prevGyroTime = new Date().getTime();

    public LocalSensorsManager() {
        sensorManager = (SensorManager) App.getInstance().getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        microphoneSensorManager = new MicrophoneSensorManager();

        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        this.lightSleepTime = state.intervals.get(SensorTypes.LIGHT.getName()).cur();
        this.accelSleepTime = state.intervals.get(SensorTypes.ACCEL.getName()).cur();
        this.gyroSleepTime = state.intervals.get(SensorTypes.GYRO.getName()).cur();
        this.microphoneSensorManager.setSleepTime(state.intervals.get(SensorTypes.MICROPHONE_DB.getName()).cur());
    }

    public void setSleepTime(final SensorTypes type, final long time) {
        switch (type) {
            case LIGHT:
                this.lightSleepTime = time;
                break;
            case ACCEL:
                this.accelSleepTime = time;
                break;
            case GYRO:
                this.gyroSleepTime = time;
                break;
            case MICROPHONE_DB:
                this.microphoneSensorManager.setSleepTime(time);
            default:
                break;
        }
    }

    public void registerListeners() {
        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();

        if (null != sensorLight) {
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
            if (state.charts.get(SensorTypes.LIGHT.getName()) == -1) {
                state.charts.put(SensorTypes.LIGHT.getName(), 1);
            }
        } else {
            state.charts.put(SensorTypes.LIGHT.getName(), -1);
        }

        if (null != sensorGyro) {
            sensorManager.registerListener(this, sensorGyro, SensorManager.SENSOR_DELAY_NORMAL);
            if (state.charts.get(SensorTypes.GYRO.getName()) == -1) {
                state.charts.put(SensorTypes.GYRO.getName(), 1);
            }
        } else {
            state.charts.put(SensorTypes.GYRO.getName(), -1);
        }

        if (null != sensorAccel) {
            sensorManager.registerListener(this, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
            if (state.charts.get(SensorTypes.ACCEL.getName()) == -1) {
                state.charts.put(SensorTypes.ACCEL.getName(), 1);
            }
        } else {
            state.charts.put(SensorTypes.ACCEL.getName(), -1);
        }

        sendInitStateBundle(state);
    }

    public void unregisterListeners() {
        sensorManager.unregisterListener(this);
    }

    private void sendInitStateBundle(final MainWebViewState state) {
        App.Preferences.writeMainWebViewState(state);
        if (null != callback) {
            final Bundle bundle = new Bundle();
            bundle.putBoolean(SensorCallback.bundleInitKey, true);
            bundle.putString(SensorCallback.bundleInitState, new Gson().toJson(state));
            callback.onReceiveResult(Activity.RESULT_OK, bundle);
        }
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
                if (currentTime - prevLightTime >= lightSleepTime) {
                    type = SensorTypes.LIGHT;
                    bundle.putSerializable(SensorCallback.bundleStatKey, new Light(vals));
                }
                break;
            case Sensor.TYPE_GYROSCOPE:
                if (currentTime - prevGyroTime >= gyroSleepTime) {
                    type = SensorTypes.GYRO;
                    gyro = AbstractSensorManager.lowPass(vals.clone(), gyro, 0.75f);
                    bundle.putSerializable(SensorCallback.bundleStatKey, new Gyro(gyro));
                }
                break;
            case Sensor.TYPE_ACCELEROMETER:
                if (currentTime - prevAccelTime >= accelSleepTime) {
                    type = SensorTypes.ACCEL;
                    accel = AbstractSensorManager.lowPass(vals.clone(), accel, 0.5f);
                    bundle.putSerializable(SensorCallback.bundleStatKey, new Accel(accel));
                    prevAccelTime = currentTime;
                }
                break;
            default:
                break;
        }
        if (null == type) {
            return;
        }

        bundle.putSerializable(SensorCallback.bundleStatType, type);
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
