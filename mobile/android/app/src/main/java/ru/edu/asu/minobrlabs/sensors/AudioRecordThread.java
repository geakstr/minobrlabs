package ru.edu.asu.minobrlabs.sensors;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

public class AudioRecordThread extends Thread implements Runnable {
    private static final String TAG = AudioRecordThread.class.getSimpleName();

    private boolean running;

    private final AudioRecordHandler audioRecordHandler;

    private int audioRecordMinSize;
    private AudioRecord audioRecord;

    public AudioRecordThread() {
        super();

        this.audioRecordHandler = new AudioRecordHandler();

        this.running = false;

        this.initAudioRecord();
    }

    public void initAudioRecord() {
        final int[] rates = new int[]{44100, 22050, 16000, 11025, 8000};
        final short[] formats = new short[]{AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT};
        final short[] channels = new short[]{AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO};

        for (int rate : rates) {
            for (short audioFormat : formats) {
                for (short channelConfig : channels) {
                    try {
                        Log.d(TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: " + channelConfig);
                        final int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);
                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);
                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                audioRecordMinSize = bufferSize;
                                audioRecord = recorder;
                                recorder.release();
                                return;
                            }
                            recorder.release();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, rate + "Exception, keep trying.", e);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            if (null != audioRecord) {
                final Bundle bundle = new Bundle();

                bundle.putInt("type", SensorTypes.MICROPHONE);
                bundle.putFloatArray("values", new float[]{getAmplitude()});

                audioRecordHandler.callback(bundle);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.e(TAG, "", e);
            }
        }
    }

    public void setRunning(final boolean running) {
        this.running = running;

        if (running) {
            if (null != this.audioRecord) {
                this.audioRecord.startRecording();
            }
        } else {
            if (null != this.audioRecord) {
                this.audioRecord.stop();
            }
        }
    }

    public void setCallback(final ISensorCallback callback) {
        this.audioRecordHandler.setCallback(callback);
    }

    private float getAmplitude() {
        short[] buffer = new short[audioRecordMinSize];
        audioRecord.read(buffer, 0, audioRecordMinSize);
        int max = 0;
        for (short s : buffer) {
            if (Math.abs(s) > max) {
                max = Math.abs(s);
            }
        }
        return (float) max;
    }
}
