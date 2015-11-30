package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.Date;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public abstract class BuiltinSensorManager{
    protected float[] prevVal;

    private final Sensor sensor;

    private long sleepTime;
    private long latestTime;

    public BuiltinSensorManager(final SensorTypes type) {
        this.sensor = null;

        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        setSleepTime(state.intervals.get(type.getName()).cur());
    }

    public BuiltinSensorManager(final SensorManager sensorManager, final SensorTypes type) {
        this.sensor = sensorManager.getDefaultSensor(type.getAndroidVal());

        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        setSleepTime(state.intervals.get(type.getName()).cur());
    }

    public void setSleepTime(final long time) {
        this.sleepTime = time;
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
        if (!period()) {
            return false;
        }
        prevVal = val.clone();
        return true;
    }

    public boolean available() {
        return sensor != null;
    }

    public Sensor getSensor() {
        return sensor;
    }
}
