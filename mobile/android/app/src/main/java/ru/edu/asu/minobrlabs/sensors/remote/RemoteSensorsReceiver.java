package ru.edu.asu.minobrlabs.sensors.remote;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import ru.edu.asu.minobrlabs.sensors.ISensorCallback;

public class RemoteSensorsReceiver extends ResultReceiver {
    private ISensorCallback callback;

    public RemoteSensorsReceiver(final Handler handler) {
        super(handler);
    }

    public void setCallback(final ISensorCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onReceiveResult(final int resultCode, final Bundle resultData) {
        if (callback != null) {
            callback.onReceiveResult(resultCode, resultData);
        }
    }
}
