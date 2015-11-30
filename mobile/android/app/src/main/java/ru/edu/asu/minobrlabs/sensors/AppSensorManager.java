package ru.edu.asu.minobrlabs.sensors;

import android.content.Intent;
import android.content.IntentFilter;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.bluetooth.BluetoothSensorsManager;
import ru.edu.asu.minobrlabs.sensors.local.LocalSensorsManager;

public class AppSensorManager {
    private final Intent sensorsServiceIntent;
    private final LocalSensorsManager localSensorsManager;
    private final BluetoothSensorsManager bluetoothSensorsManager;

    public AppSensorManager() {
        this.sensorsServiceIntent = new Intent(App.state().getActivity(), SensorsService.class);

        this.localSensorsManager = new LocalSensorsManager();
        this.bluetoothSensorsManager = new BluetoothSensorsManager();
    }

    public void init() {
        App.state().getActivity().startService(sensorsServiceIntent);
        App.state().getActivity().registerReceiver(localSensorsManager.broadcastReceiver, new IntentFilter(SensorsService.BROADCAST_ACTION));

        localSensorsManager.registerListeners();
    }

    public void destroy() {
        localSensorsManager.unregisterListeners();

        App.state().getActivity().unregisterReceiver(localSensorsManager.broadcastReceiver);
        App.state().getActivity().stopService(sensorsServiceIntent);
    }

    public LocalSensorsManager getLocalSensorsManager() {
        return localSensorsManager;
    }

    public BluetoothSensorsManager getBluetoothSensorsManager() {
        return bluetoothSensorsManager;
    }

    public static float[] lowPass(final float[] input, final float[] output, final float alpha) {
        if (output == null) {
            return input;
        }
        for (int i = 0; i < input.length; i++) {
            output[i] = lowPass(input[i], output[i], alpha);
        }
        return output;
    }

    public static float lowPass(final float input, final float output, final float alpha) {
        return alpha * output + (1 - alpha) * input;
    }
}
