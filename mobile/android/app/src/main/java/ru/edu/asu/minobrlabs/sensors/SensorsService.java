package ru.edu.asu.minobrlabs.sensors;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import ru.edu.asu.minobrlabs.App;

public class SensorsService extends Service {
    public static final String BROADCAST_ACTION = "ru.edu.asu.minobrlabs.sensors.SensorsService";

    private final Handler handler = new Handler();
    private final Intent intent = new Intent(BROADCAST_ACTION);
    private final Runnable runnable = new Runnable() {
        public void run() {
            App.state.appSensorsManager.localSensorsManager.update();

            sendBroadcast(intent);

            handler.postDelayed(this, 20L);
        }
    };

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 0);

        return START_STICKY;
    }
}
