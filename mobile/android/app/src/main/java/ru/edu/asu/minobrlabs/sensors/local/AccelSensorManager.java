package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Accel;
import ru.edu.asu.minobrlabs.sensors.AppSensorManager;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class AccelSensorManager extends BuiltinSensorManager {
    public AccelSensorManager(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.ACCEL);
    }

    @Override
    public boolean update(final float[] val) {
        if (super.update(val)) {
            prevVal = AppSensorManager.lowPass(val, prevVal, 0.75f);
            App.state().getSensorsState().update(SensorTypes.ACCEL, new Accel(prevVal));
            return true;
        }
        return false;
    }
}
