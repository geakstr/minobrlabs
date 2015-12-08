package ru.edu.asu.minobrlabs.sensors.common;

import android.util.Log;

import ru.edu.asu.minobrlabs.App;

public abstract class AbstractSensorsWorker<T extends ISensorsManager> extends AbstractWorker {
    private final String tag;

    private final T sensors;

    public AbstractSensorsWorker(final T sensors, final String tag) {
        super();

        this.sensors = sensors;
        this.tag = tag;
    }

    @Override
    public void start() {
        sensors.start();

        super.start();
    }

    @Override
    public void run() {
        try {
            while (running) {
                sensors.update();

                sleep(App.state.storage.sleepTime);
            }
        } catch (InterruptedException e) {
            Log.e(tag, "Sensors worker problems", e);
        }
    }

    @Override
    public void kill() {
        super.kill();
    }
}
