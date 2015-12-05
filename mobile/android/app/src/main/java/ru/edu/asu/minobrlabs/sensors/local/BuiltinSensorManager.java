package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public abstract class BuiltinSensorManager {
    public float[] val;

    private final Sensor sensor;

    public BuiltinSensorManager(final int size) {
        this.sensor = null;
        this.val = new float[size];
    }

    public BuiltinSensorManager(final SensorManager sensorManager, final SensorTypes.Type type, final int size) {
        this.sensor = sensorManager.getDefaultSensor(type.id);
        this.val = new float[size];
    }

    public void setVal(final float[] val) {
        this.val = val;
    }

    public boolean update(final float[] val) {
        this.val = val;
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
