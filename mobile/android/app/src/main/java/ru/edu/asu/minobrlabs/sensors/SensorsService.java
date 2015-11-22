package ru.edu.asu.minobrlabs.sensors;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class SensorsService extends Service {
    public static final String BROADCAST_ACTION = "ru.edu.asu.minobrlabs.sensors.SensorsService";

    private long sleepTime = 300L;
    private final Handler handler = new Handler();
    private final Intent intent = new Intent(BROADCAST_ACTION);
    private final Runnable runnable = new Runnable() {
        public void run() {
            intent.putExtra("counter", String.valueOf(++counter));
            sendBroadcast(intent);

            handler.postDelayed(this, sleepTime);
        }
    };

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    private int counter = 0;

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);

        sleepTime = intent.getLongExtra("sleepTime", 300L);

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 0);

        return START_STICKY;
    }
}
