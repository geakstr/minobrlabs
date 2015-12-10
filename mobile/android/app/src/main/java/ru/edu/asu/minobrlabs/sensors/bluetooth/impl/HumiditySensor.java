package ru.edu.asu.minobrlabs.sensors.bluetooth.impl;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.common.AbstractSensor;

public class HumiditySensor extends AbstractSensor {
    public HumiditySensor() {
        super(1);
    }

    @Override
    public boolean update(final float val) {
        super.update(val);

        App.state.storage.push(SensorTypes.HUMIDITY.id, this.val);

        return true;
    }
}
