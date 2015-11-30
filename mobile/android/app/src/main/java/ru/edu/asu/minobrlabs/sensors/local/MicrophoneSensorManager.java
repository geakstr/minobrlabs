package ru.edu.asu.minobrlabs.sensors.local;

import android.media.MediaRecorder;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Microphone;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class MicrophoneSensorManager extends BuiltinSensorManager {
    private MediaRecorder mediaRecorder;

    public MicrophoneSensorManager() {
        super(SensorTypes.MICROPHONE_DB);
    }

    @Override
    public boolean available() {
        return startMediaRecorder();
    }

    @Override
    public boolean update(float[] val) {
        return true;
    }

    public void unregisterListener() {
        stopMediaRecorder();
    }

    public void update() {
        if (!period()) {
            return;
        }

        // Thanks Lukas Ruge for pressure and decibel formulas
        // http://stackoverflow.com/questions/10655703/what-does-androids-getmaxamplitude-function-for-the-mediarecorder-actually-gi
        final double pressure = mediaRecorder.getMaxAmplitude() / 51805.5336; // 51805.5336 = 32767 / 0.6325 where 0.6325 Pa equals 90 dB;
        int db = (int) (20 * Math.log10(pressure / 0.00002)); // Convert pressure to dB (SPL)
        db = db < 0 ? 40 : db;

        App.state().getSensorsState().update(SensorTypes.MICROPHONE_DB, new Microphone(new float[]{db}));
    }

    private boolean startMediaRecorder() {
        if (null != mediaRecorder) {
            return true;
        }

        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        try {
            mediaRecorder = new MediaRecorder();

            mediaRecorder.reset();

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile("/dev/null/");

            mediaRecorder.prepare();
            mediaRecorder.start();

            if (state.charts.get(SensorTypes.MICROPHONE_DB.getName()) == -1) {
                state.charts.put(SensorTypes.MICROPHONE_DB.getName(), 1);
            }
        } catch (Exception e) {
            // Log.e(tag, e.getMessage(), e);
            state.charts.put(SensorTypes.MICROPHONE_DB.getName(), -1);
            return false;
        } finally {
            App.Preferences.writeMainWebViewState(state);
            App.state().getSensorsState().wantReInit = true;
        }
        return true;
    }

    private void stopMediaRecorder() {
        if (null != mediaRecorder) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
