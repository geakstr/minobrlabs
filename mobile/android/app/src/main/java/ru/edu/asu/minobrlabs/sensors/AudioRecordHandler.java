package ru.edu.asu.minobrlabs.sensors;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class AudioRecordHandler extends Handler {
    private ISensorCallback callback;

    public void setCallback(final ISensorCallback callback) {
        this.callback = callback;
    }

    @Override
    public void handleMessage(final Message msg) {
        final Bundle bundle = msg.getData().getBundle("bundle");
        if (null != bundle) {
            callback.onReceiveResult(Activity.RESULT_OK, bundle);
        }
    }

    public void callback(final Bundle callbackBundle) {
        final Bundle bundle = new Bundle();
        bundle.putBundle("bundle", callbackBundle);

        final Message msg = obtainMessage();
        msg.setData(bundle);

        sendMessage(msg);
    }
}
