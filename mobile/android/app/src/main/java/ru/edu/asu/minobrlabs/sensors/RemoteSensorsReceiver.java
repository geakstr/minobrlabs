package ru.edu.asu.minobrlabs.sensors;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class RemoteSensorsReceiver extends ResultReceiver {
    private WebViewSensorCallback receiver;

    public RemoteSensorsReceiver(final Handler handler) {
        super(handler);
    }

    public void setReceiver(final WebViewSensorCallback receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void onReceiveResult(final int resultCode, final Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
}
