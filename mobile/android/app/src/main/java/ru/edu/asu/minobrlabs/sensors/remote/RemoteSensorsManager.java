package ru.edu.asu.minobrlabs.sensors.remote;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;

public class RemoteSensorsManager extends Thread {
    private static final String TAG = RemoteSensorsManager.class.getSimpleName();

    private long sleepTime;
    private boolean running;

    private final Context context;
    private final RemoteSensorsReceiver receiver;

    public RemoteSensorsManager(final Context context) {
        super();

        this.context = context;
        this.receiver = new RemoteSensorsReceiver(new Handler());

        this.sleepTime = 300L;
        this.running = false;
    }

    public void setSleepTime(final long time) {
        if (time < 100L || time > 10000L) {
            final String message = context.getString(R.string.detectors_sleep_time_exception);
            throw new IllegalArgumentException(String.format(message, "0.1", "10000"));
        }
        this.sleepTime = time;
    }

    @Override
    public void run() {
        while (running) {
            final Intent i = new Intent(context, RemoteSensorsService.class);
            i.putExtra("receiver", receiver);
            context.startService(i);

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Log.e(TAG, "", e);
            }
        }
    }

    public void setCallback(final ISensorCallback callback) {
        receiver.setReceiver(callback);
    }

    public void setRunning(final boolean running) {
        if (!this.running && running) {
            super.start();
        }

        this.running = running;
    }
}
