package ru.edu.asu.minobrlabs.sensors.local;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import ru.edu.asu.minobrlabs.sensors.ISensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class MicrophoneSensorThread extends Thread implements Runnable {
    private static final String TAG = MicrophoneSensorThread.class.getSimpleName();

    private boolean running;

    private final MicrophoneSensorHandler microphoneSensorHandler;
    private MediaRecorder mediaRecorder;

    public MicrophoneSensorThread() {
        super();

        running = false;
        microphoneSensorHandler = new MicrophoneSensorHandler();
    }

    private void startMediaRecorder() {
        if (null != mediaRecorder) {
            return;
        }

        mediaRecorder = new MediaRecorder();

        mediaRecorder.reset();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile("/dev/null/");

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
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
                Log.e(TAG, "", e);
            }
        }
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

    public void setRunning(final boolean running) {
        this.running = running;

        if (running) {
            startMediaRecorder();
        } else {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public void setCallback(final ISensorCallback callback) {
        microphoneSensorHandler.setCallback(callback);
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
