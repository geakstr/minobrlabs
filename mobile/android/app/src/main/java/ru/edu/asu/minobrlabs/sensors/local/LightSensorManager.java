package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Light;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class LightSensorManager extends BuiltinSensorManager {
    public LightSensorManager(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.LIGHT, 100, 1);
    }

    @Override
    public boolean update(float[] val) {
        val = new float[]{val[0]};
        if (super.update(val)) {
            App.state.sensors.update(SensorTypes.LIGHT, new Light(this.val));
            return true;
        }
        return false;
    }
}
