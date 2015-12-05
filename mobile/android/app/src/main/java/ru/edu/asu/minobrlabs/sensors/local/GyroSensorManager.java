package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class GyroSensorManager extends BuiltinSensorManager {
    public GyroSensorManager(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.GYRO, 3);
    }

    @Override
    public boolean update(final float[] val) {
        super.update(val);

        //this.val = AppSensorsManager.lowPass(val, this.val, 0.5f);
        App.state.storage.push(SensorTypes.GYRO.id, this.val);

        return true;
    }
}
