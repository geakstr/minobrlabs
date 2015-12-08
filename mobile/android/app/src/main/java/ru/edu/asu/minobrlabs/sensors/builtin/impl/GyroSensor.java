package ru.edu.asu.minobrlabs.sensors.builtin.impl;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.builtin.BuiltinSensor;

public class GyroSensor extends BuiltinSensor {
    public GyroSensor(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.GYRO, 3);
    }

    @Override
    public boolean update(final float[] val) {
        super.update(val);

        App.state.storage.push(SensorTypes.GYRO.id, this.val);

        return true;
    }
}
