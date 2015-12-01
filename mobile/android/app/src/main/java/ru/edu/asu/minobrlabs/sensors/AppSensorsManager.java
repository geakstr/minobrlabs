package ru.edu.asu.minobrlabs.sensors;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.bluetooth.BluetoothSensorsManager;
import ru.edu.asu.minobrlabs.sensors.local.LocalSensorsManager;

public class AppSensorsManager {
    public final LocalSensorsManager localSensorsManager;
    public final BluetoothSensorsManager bluetoothSensorsManager;

    private final Intent sensorsServiceIntent;

    public AppSensorsManager() {
        this.sensorsServiceIntent = new Intent(App.instance.getApplicationContext(), SensorsService.class);

        this.localSensorsManager = new LocalSensorsManager();
        this.bluetoothSensorsManager = new BluetoothSensorsManager();
    }

    public void init(final BroadcastReceiver broadcastReceiver) {
        App.state.activity.startService(sensorsServiceIntent);
        App.state.activity.registerReceiver(broadcastReceiver, new IntentFilter(SensorsService.BROADCAST_ACTION));

        localSensorsManager.registerListeners();
    }

    public void destroy(final BroadcastReceiver broadcastReceiver) {
        localSensorsManager.unregisterListeners();

        App.state.activity.unregisterReceiver(broadcastReceiver);
        App.state.activity.stopService(sensorsServiceIntent);
    }

    public void setSleepTime(final long time) {
        localSensorsManager.setSleepTime(time);
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
