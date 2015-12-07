package ru.edu.asu.minobrlabs.sensors.local;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorsService;
import ru.edu.asu.minobrlabs.sensors.bluetooth.BluetoothConnector;

public class LocalSensorsWorker {
    private final Handler handler;
    private final Intent intent;
    private final Runnable runnable;

    public LocalSensorsWorker() {
        this.handler = new Handler();
        this.intent = new Intent(SensorsService.BROADCAST_ACTION);
        this.runnable = new Runnable() {
            public void run() {
                App.state.appSensorsManager.localSensorsManager.update();

                App.instance.getApplicationContext().sendBroadcast(intent);
                handler.postDelayed(this, App.state.storage.sleepTime);
            }
        };
    }

    public void start() {
        start(0);
    }

    public void start(final long sleepTime) {
        handler.postDelayed(runnable, sleepTime);
    }

    public void stop() {
        handler.removeCallbacks(runnable);

        App.state.storage.stopRecording();
    }

    public void restart() {
        stop();
        start();
    }
}
