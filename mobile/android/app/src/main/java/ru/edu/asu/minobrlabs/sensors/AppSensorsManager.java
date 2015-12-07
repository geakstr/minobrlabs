package ru.edu.asu.minobrlabs.sensors;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.bluetooth.BluetoothSensorsWorker;
import ru.edu.asu.minobrlabs.sensors.local.LocalSensorsManager;

public class AppSensorsManager {
    public static final String TAG = AppSensorsManager.class.getSimpleName();

    public final LocalSensorsManager localSensorsManager;

    public final BluetoothSensorsWorker bluetoothSensorsWorker;

    private final Intent sensorsServiceIntent;
    private boolean initiated;

    public AppSensorsManager() {
        this.sensorsServiceIntent = new Intent(App.instance.getApplicationContext(), SensorsService.class);

        this.localSensorsManager = new LocalSensorsManager();
        this.bluetoothSensorsWorker = new BluetoothSensorsWorker();

        this.initiated = false;
    }

    public void start(final BroadcastReceiver broadcastReceiver) {
        if (!initiated) {
            bluetoothSensorsWorker.start();

            App.state.activity.startService(sensorsServiceIntent);
            App.state.activity.registerReceiver(broadcastReceiver, new IntentFilter(SensorsService.BROADCAST_ACTION));

            localSensorsManager.registerListeners();

            initiated = true;

            Log.d(TAG, "Initialize");
        }
    }

    public void stop(final BroadcastReceiver broadcastReceiver) {
        if (initiated) {
            localSensorsManager.unregisterListeners();

            App.state.activity.unregisterReceiver(broadcastReceiver);
            App.state.activity.stopService(sensorsServiceIntent);
            App.state.localSensorsWorker.stop();

            initiated = false;

            Log.d(TAG, "Deinitialize");
        }
    }

    public void setSleepTime(final long time) {
        App.state.storage.sleepTime = time;
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
