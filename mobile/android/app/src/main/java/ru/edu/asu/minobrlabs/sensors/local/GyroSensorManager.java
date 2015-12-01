package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Gyro;
import ru.edu.asu.minobrlabs.sensors.AppSensorsManager;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class GyroSensorManager extends BuiltinSensorManager {
    public GyroSensorManager(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.GYRO, 40, 3);
    }

    @Override
    public boolean update(final float[] val) {
        if (super.update(val)) {
            this.val = AppSensorsManager.lowPass(val, this.val, 0.5f);
            App.state.sensors.update(SensorTypes.GYRO, new Gyro(this.val));
            return true;
        }
        return false;
    }
}
