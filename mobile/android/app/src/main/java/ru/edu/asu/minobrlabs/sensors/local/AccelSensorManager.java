package ru.edu.asu.minobrlabs.sensors.local;

import android.hardware.SensorManager;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Accel;
import ru.edu.asu.minobrlabs.sensors.AppSensorsManager;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class AccelSensorManager extends BuiltinSensorManager {
    public AccelSensorManager(final SensorManager sensorManager) {
        super(sensorManager, SensorTypes.ACCEL, 40, 3);
    }

    public void setVal(float[] val) {
        this.val = normalize(val);
    }

    @Override
    public boolean update(float[] val) {
        if (super.update(val)) {
            this.val = AppSensorsManager.lowPass(val, this.val, 0.75f);
            App.state.sensors.update(SensorTypes.ACCEL, new Accel(this.val));
            return true;
        }
        return false;
    }

    private float[] normalize(final float[] val) {
        for (int i = 0; i < 3; i++) {
            val[i] /= 9.81f;
        }
        return val;
    }
}
