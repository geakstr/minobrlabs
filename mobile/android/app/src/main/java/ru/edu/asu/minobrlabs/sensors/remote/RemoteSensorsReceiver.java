package ru.edu.asu.minobrlabs.sensors.remote;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import ru.edu.asu.minobrlabs.sensors.ISensorCallback;

public class RemoteSensorsReceiver extends ResultReceiver {
    private ISensorCallback receiver;

    public RemoteSensorsReceiver(final Handler handler) {
        super(handler);
    }

    public void setReceiver(final ISensorCallback receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void onReceiveResult(final int resultCode, final Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
}
