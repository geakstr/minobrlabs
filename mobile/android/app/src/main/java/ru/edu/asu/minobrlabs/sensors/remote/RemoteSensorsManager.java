package ru.edu.asu.minobrlabs.sensors.remote;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import ru.edu.asu.minobrlabs.Application;
import ru.edu.asu.minobrlabs.sensors.AbstractSensorManager;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;

public class RemoteSensorsManager extends AbstractSensorManager {
    private final RemoteSensorsReceiver receiver;

    public RemoteSensorsManager() {
        super(RemoteSensorsManager.class.getSimpleName());

        this.receiver = new RemoteSensorsReceiver(new Handler());
    }

    @Override
    public void run() {
        final Context context = Application.getInstance().getApplicationContext();
        while (isRunning()) {
            final Intent i = new Intent(context, RemoteSensorsService.class);
            i.putExtra("receiver", receiver);
            context.startService(i);

            sleep();
        }
    }

    @Override
    public void setCallback(final ISensorCallback callback) {
        receiver.setCallback(callback);
    }
}
