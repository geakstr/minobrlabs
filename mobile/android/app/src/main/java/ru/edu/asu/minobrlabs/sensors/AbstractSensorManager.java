package ru.edu.asu.minobrlabs.sensors;

import android.util.Log;

public abstract class AbstractSensorManager implements Runnable {
    protected final String tag;

    private volatile boolean running;
    private long sleepTime;

    public AbstractSensorManager(final String tag) {
        this.tag = tag;
        this.running = false;
        this.sleepTime = 300L;
    }

    @Override
    public abstract void run();

    public abstract void setCallback(final ISensorCallback callback);

    public void start() {
        if (!running) {
            new Thread(this).start();
        }
        running = true;
    }

    public void stop() {
        running = false;
    }

    public void setSleepTime(final long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public boolean isRunning() {
        return running;
    }

    public static float[] lowPass(final float[] input, final float[] output, final float alpha) {
        if (output == null) {
            return input;
        }
        for (int i = 0; i < input.length; i++) {
            output[i] = lowPass(input[i], output[i], alpha);
        }
        return output;
    }

    public static float lowPass(final float input, final float output, final float alpha) {
        return alpha * output + (1 - alpha) * input;
    }

    protected void sleep() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Log.e(tag, e.getMessage(), e);
        }
    }
}
