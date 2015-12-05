package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class AccelSensorManager extends BuiltinSensorManager {
    public AccelSensorManager(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.ACCEL, 3);
    }

    @Override
    public void setVal(float[] val) {
        val[0] /= 9.81f;
        val[1] /= 9.81f;
        val[2] /= 9.81f;
        this.val = val;
    }

    @Override
    public boolean update(float[] val) {
        super.update(val);

        //this.val = AppSensorsManager.lowPass(val, this.val, 0.75f);
        App.state.storage.push(SensorTypes.ACCEL.id, this.val);

        return true;
    }
}
