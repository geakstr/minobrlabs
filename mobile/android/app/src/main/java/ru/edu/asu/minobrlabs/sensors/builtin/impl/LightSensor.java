package ru.edu.asu.minobrlabs.sensors.builtin.impl;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.builtin.BuiltinSensor;

public class LightSensor extends BuiltinSensor {
    public LightSensor(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.LIGHT, 1);
    }

    @Override
    public boolean update(final float[] val) {
        super.update(new float[]{val[0]});

        App.state.storage.push(SensorTypes.LIGHT.id, this.val);

        return true;
    }
}
