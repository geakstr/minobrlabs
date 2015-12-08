package ru.edu.asu.minobrlabs.sensors.bluetooth;

import ru.edu.asu.minobrlabs.sensors.common.AbstractSensorsWorker;

public class BluetoothSensorsWorker extends AbstractSensorsWorker<BluetoothSensorsManager> {
    public static final String TAG = BluetoothSensorsWorker.class.getSimpleName();

    public BluetoothSensorsWorker(final BluetoothSensorsManager bluetoothSensorsManager) {
        super(bluetoothSensorsManager, TAG);
    }
}
