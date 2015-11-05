package ru.edu.asu.minobrlabs.detectors;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import ru.edu.asu.minobrlabs.R;

public class DetectorsThread extends Thread {
    private static final String TAG = DetectorsThread.class.getSimpleName();

    private final Context context;
    private final DetectorsReceiver receiver;

    private Long sleepTime;

    public DetectorsThread(final Context context) {
        this.context = context;
        this.receiver = new DetectorsReceiver(new Handler());
        this.sleepTime = 300L;
    }

    public void setSleepTime(final Long time) {
        if (time < 100L || time > 10000L) {
            final String message = context.getString(R.string.detectors_sleep_time_exception);
            throw new IllegalArgumentException(String.format(message, "0.1", "10000"));
        }
        this.sleepTime = time;
    }

    @Override
    public void run() {
        while (true) {
            final Intent i = new Intent(context, DetectorsService.class);
            i.putExtra("receiver", receiver);
            context.startService(i);

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Log.e(TAG, "", e);
            }
        }
    }

    public void start(final DetectorsReceiver.Callback callback) {
        receiver.setReceiver(callback);

        super.start();
    }
}
