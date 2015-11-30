package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Gyro;
import ru.edu.asu.minobrlabs.sensors.AppSensorsManager;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class GyroSensorManager extends BuiltinSensorManager {
    public GyroSensorManager(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.GYRO);
    }

    @Override
    public boolean update(final float[] val) {
        if (super.update(val)) {
            prevVal = AppSensorsManager.lowPass(val, prevVal, 0.5f);
            App.state.sensors.update(SensorTypes.GYRO, new Gyro(prevVal));
            return true;
        }
        return false;
    }
}
