package ru.edu.asu.minobrlabs.sensors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import ru.edu.asu.minobrlabs.App;

public class SensorsService extends Service {
    public static final String BROADCAST_ACTION = "ru.edu.asu.minobrlabs.sensors.SensorsService";

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);

        App.state.appSensorsThread.restart();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        App.state.appSensorsThread.stop();
    }
}
