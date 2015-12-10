package ru.edu.asu.minobrlabs.sensors.builtin.impl;

import android.Manifest;
import android.media.MediaRecorder;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.builtin.BuiltinSensor;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class MicrophoneSensor extends BuiltinSensor {
    public static final String TAG = MicrophoneSensor.class.getSimpleName();

    private final PermissionListener recordAudioPermissionListener;
    private MediaRecorder mediaRecorder;
    private boolean available;
    private double lastPressure;

    public MicrophoneSensor() {
        super(1);

        this.available = false;
        this.lastPressure = 0;
        this.recordAudioPermissionListener = new RecordAudioPermissionListener();
    }

    @Override
    public boolean available() {
        return startMediaRecorder();
    }

    @Override
    public boolean update(float[] val) {
        return true;
    }

    @Override
    public boolean update() {
        if (null == mediaRecorder) {
            return false;
        }

        try {
            // Thanks Lukas Ruge for formulas
            // http://stackoverflow.com/questions/10655703/what-does-androids-getmaxamplitude-function-for-the-mediarecorder-actually-gi
            // 51805.5336 = 32767 / 0.6325 where 0.6325 Pa equals 90 dB;
            final double pressure = Math.abs(mediaRecorder.getMaxAmplitude()) / 51805.5336;

            if (pressure >= 0.00002) {
                lastPressure = pressure;
            }

            final int db = (int) (20 * Math.log10(lastPressure / 0.00002)); // Convert pressure to dB (SPL)
            App.state.storage.push(SensorTypes.MICROPHONE_DB.id, new float[]{db < 0 ? 20 : db});
        } catch (Exception e) {
            Log.e(TAG, "getMaxAmplitude problem", e);
        }

        return true;
    }

    public void unregisterListener() {
        stopMediaRecorder();
    }

    private boolean startMediaRecorder() {
        if (null != mediaRecorder) {
            return true;
        }

        Dexter.checkPermission(recordAudioPermissionListener, Manifest.permission.RECORD_AUDIO);

        return available;
    }

    private void stopMediaRecorder() {
        if (null != mediaRecorder) {
            try {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
            } catch (IllegalStateException e) {
                Log.e(TAG, "Not available", e);
            }
        }
    }

    public class RecordAudioPermissionListener implements PermissionListener {
        @Override
        public void onPermissionGranted(PermissionGrantedResponse response) {
            Log.d(TAG, "RecordAudio permission granted");

            final MainWebViewState state = App.state.webViewState;
            try {
                if (null != mediaRecorder) {
                    available = true;
                    return;
                }

                mediaRecorder = new MediaRecorder();

                mediaRecorder.reset();

                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile("/dev/null/");

                mediaRecorder.prepare();
                mediaRecorder.start();

                if (state.charts.get(SensorTypes.MICROPHONE_DB.name) == -1) {
                    state.charts.put(SensorTypes.MICROPHONE_DB.name, 1);
                }

                available = true;
            } catch (Exception e) {
                state.charts.put(SensorTypes.MICROPHONE_DB.name, -1);
                available = false;
            } finally {
                App.state.setWebViewState(state);
                App.state.storage.wantReInit = true;
            }
        }

        @Override
        public void onPermissionDenied(PermissionDeniedResponse response) {
            Log.d(TAG, "RecordAudio permission denied");

            final MainWebViewState state = App.state.webViewState;
            state.charts.put(SensorTypes.MICROPHONE_DB.name, -1);
            App.state.setWebViewState(state);

            available = false;
        }

        @Override
        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        }
    }
}
