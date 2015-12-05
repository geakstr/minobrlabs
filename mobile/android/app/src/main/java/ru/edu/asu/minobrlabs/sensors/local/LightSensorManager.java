package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class LightSensorManager extends BuiltinSensorManager {
    public LightSensorManager(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.LIGHT, 1);
    }

    @Override
    public boolean update(float[] val) {
        super.update(new float[]{val[0]});

        App.state.storage.push(SensorTypes.LIGHT.id, this.val);

        return true;
    }
}
