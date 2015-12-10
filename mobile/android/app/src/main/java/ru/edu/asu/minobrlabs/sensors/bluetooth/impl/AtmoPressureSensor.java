package ru.edu.asu.minobrlabs.sensors.bluetooth.impl;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.common.AbstractSensor;

public class AtmoPressureSensor extends AbstractSensor {
    public AtmoPressureSensor() {
        super(1);
    }

    @Override
    public boolean update(final float val) {
        super.update(val);

        App.state.storage.push(SensorTypes.ATMO_PRESSURE.id, this.val);

        return true;
    }
}
