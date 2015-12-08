package ru.edu.asu.minobrlabs.sensors.common;

public abstract class AbstractWorker extends Thread implements Runnable {
    protected volatile boolean running;

    public AbstractWorker() {
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
