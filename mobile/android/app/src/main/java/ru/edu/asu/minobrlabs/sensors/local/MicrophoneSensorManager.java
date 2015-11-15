package ru.edu.asu.minobrlabs.sensors.local;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import ru.edu.asu.minobrlabs.db.entities.params.Microphone;
import ru.edu.asu.minobrlabs.sensors.AbstractSensorManager;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class MicrophoneSensorManager extends AbstractSensorManager {
    private MediaRecorder mediaRecorder;
    private final MicrophoneSensorHandler microphoneSensorHandler;

    public MicrophoneSensorManager() {
        super(MicrophoneSensorManager.class.getSimpleName());

        this.microphoneSensorHandler = new MicrophoneSensorHandler();
    }

    @Override
    public void run() {
        while (isRunning()) {
            final Bundle bundle = new Bundle();
            bundle.putSerializable(SensorCallback.bundleKey, new Microphone(new float[]{getDecibel()}));
            bundle.putString(SensorCallback.bundleType, SensorTypes.MICROPHONE_DB);

            microphoneSensorHandler.apply(bundle);

            sleep();
        }
    }

    @Override
    public void start() {
        startMediaRecorder();
        super.start();
    }

    @Override
    public void stop() {
        stopMediaRecorder();
        super.stop();
    }

    @Override
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
            Log.e(tag, e.getMessage(), e);
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
        return mediaRecorder.getMaxAmplitude() / 51805.5336; // 51805.5336 = 32767 / 0.6325 where 0.6325 Pa equals 90 dB
    }

    private int getDecibel() {
        // Db
        return (int) (20 * Math.log10(getPressure() / 0.00002)); // Convert pressure to dB (SPL)
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
