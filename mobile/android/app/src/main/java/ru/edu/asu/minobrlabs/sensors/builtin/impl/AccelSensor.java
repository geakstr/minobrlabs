package ru.edu.asu.minobrlabs.sensors.builtin.impl;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.builtin.BuiltinSensor;

public class AccelSensor extends BuiltinSensor {
    public AccelSensor(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.ACCEL, 3);
    }

    @Override
    public void setVal(final float[] val) {
        val[0] /= 9.81f;
        val[1] /= 9.81f;
        val[2] /= 9.81f;
        super.setVal(val);
    }

    @Override
    public boolean update(final float[] val) {
        // Not update because setVal before
        // super.update();

        App.state.storage.push(SensorTypes.ACCEL.id, this.val);

        return true;
    }
}
