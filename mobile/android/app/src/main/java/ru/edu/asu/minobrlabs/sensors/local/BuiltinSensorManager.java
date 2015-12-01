package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.Date;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public abstract class BuiltinSensorManager {
    public float[] val;

    protected final long minSleepTime;

    private final Sensor sensor;

    private long sleepTime;
    private long latestTime;

    public BuiltinSensorManager(final long minSleepTime, final int size) {
        this.sensor = null;

        this.minSleepTime = minSleepTime;

        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        setSleepTime(state.getCurrentInterval());

        this.val = new float[size];
    }

    public BuiltinSensorManager(final SensorManager sensorManager, final SensorTypes type, final long minSleepTime, final int size) {
        this.sensor = sensorManager.getDefaultSensor(type.getAndroidVal());

        this.minSleepTime = minSleepTime;

        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        setSleepTime(state.getCurrentInterval());

        this.val = new float[size];
    }

    public void setVal(final float[] val) {
        this.val = val;
    }

    public void setSleepTime(final long time) {
        this.sleepTime = Math.max(time, minSleepTime);
    }

    public boolean period() {
        final long currentTime = new Date().getTime();
        if (currentTime - latestTime >= sleepTime) {
            latestTime = currentTime;
            return true;
        }
        return false;
    }

    public boolean update(final float[] val) {
        if (!period() || null == val) {
            return false;
        }
        this.val = val.clone();
        return true;
    }

    public boolean update() {
        return update(val);
    }

    public boolean available() {
        return sensor != null;
    }

    public Sensor getSensor() {
        return sensor;
    }
}
