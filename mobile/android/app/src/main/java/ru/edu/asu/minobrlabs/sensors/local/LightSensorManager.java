package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Light;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class LightSensorManager extends BuiltinSensorManager {
    public LightSensorManager(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.LIGHT);
    }

    @Override
    public boolean update(final float[] val) {
        if (super.update(val)) {
            App.state().getSensorsState().update(SensorTypes.LIGHT, new Light(prevVal));
            return true;
        }
        return false;
    }
}
