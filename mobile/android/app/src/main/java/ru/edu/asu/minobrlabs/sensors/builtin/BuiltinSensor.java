package ru.edu.asu.minobrlabs.sensors.builtin;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.common.AbstractSensor;

public abstract class BuiltinSensor extends AbstractSensor {
    private final Sensor sensor;

    public BuiltinSensor(final int size) {
        super(size);
        this.sensor = null;
    }

    public BuiltinSensor(final SensorManager sensorManager, final SensorTypes.Type type, final int size) {
        super(size);
        this.sensor = sensorManager.getDefaultSensor(type.id);
    }

    public boolean available() {
        return sensor != null;
    }

    public Sensor getSensor() {
        return sensor;
    }
}
