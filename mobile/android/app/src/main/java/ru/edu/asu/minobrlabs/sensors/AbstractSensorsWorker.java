package ru.edu.asu.minobrlabs.sensors;

public abstract class AbstractSensorsWorker extends Thread implements Runnable {
    protected volatile boolean running;

    public AbstractSensorsWorker() {
        this.running = false;
    }

    @Override
    public abstract void run();

    @Override
    public void start() {
        if (!running) {
            running = true;
            super.start();
        }
    }

    public void kill() {
        running = false;
    }
}
