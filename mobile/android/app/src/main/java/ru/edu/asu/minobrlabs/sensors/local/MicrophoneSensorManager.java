package ru.edu.asu.minobrlabs.sensors.local;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class MicrophoneSensorManager extends Thread implements Runnable {
    private static final String TAG = MicrophoneSensorManager.class.getSimpleName();

    private long sleepTime;
    private boolean running;

    private final Context context;
    private MediaRecorder mediaRecorder;
    private final MicrophoneSensorHandler microphoneSensorHandler;

    public MicrophoneSensorManager(final Context context) {
        super();

        this.context = context;
        this.microphoneSensorHandler = new MicrophoneSensorHandler();

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
            final Bundle bundle = new Bundle();

            bundle.putInt("type", SensorTypes.MICROPHONE_DB);
            bundle.putFloatArray("values", new float[]{getDecibel()});

            microphoneSensorHandler.apply(bundle);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public void setRunning(final boolean running) {
        if (running) {
            startMediaRecorder();
        } else {
            stopMediaRecorder();
        }

        if (!this.running && running) {
            super.start();
        }

        this.running = running;
    }

    public void setCallback(final ISensorCallback callback) {
        microphoneSensorHandler.setCallback(callback);
    }

    private void startMediaRecorder() {
        if (null != mediaRecorder) {
            return;
        }

        try {
            mediaRecorder = new MediaRecorder();

            mediaRecorder.reset();

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile("/dev/null/");

            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void stopMediaRecorder() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    // Thanks Lukas Ruge for pressure and decibel formulas
    // http://stackoverflow.com/questions/10655703/what-does-androids-getmaxamplitude-function-for-the-mediarecorder-actually-gi
    private double getPressure() {
        // Pa
        return mediaRecorder.getMaxAmplitude() / 51805.5336;
    }

    private int getDecibel() {
        // Db
        return (int) (20 * Math.log10(getPressure() / 0.00002));
    }

    private static class MicrophoneSensorHandler extends Handler {
        private ISensorCallback callback;

        public MicrophoneSensorHandler() {
            super();
        }

        public void setCallback(final ISensorCallback callback) {
            this.callback = callback;
        }

        @Override
        public void handleMessage(final Message msg) {
            final Bundle bundle = msg.getData().getBundle("bundle");
            if (null != bundle) {
                callback.onReceiveResult(Activity.RESULT_OK, bundle);
            }
        }

        public void apply(final Bundle callbackBundle) {
            final Bundle bundle = new Bundle();
            bundle.putBundle("bundle", callbackBundle);

            final Message msg = obtainMessage();
            msg.setData(bundle);

            sendMessage(msg);
        }
    }
}
