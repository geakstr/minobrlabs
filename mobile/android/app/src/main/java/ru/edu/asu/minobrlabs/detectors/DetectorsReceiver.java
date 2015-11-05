package ru.edu.asu.minobrlabs.detectors;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class DetectorsReceiver extends ResultReceiver {
    private Callback receiver;

    public DetectorsReceiver(final Handler handler) {
        super(handler);
    }

    public void setReceiver(final Callback receiver) {
        this.receiver = receiver;
    }

    public interface Callback {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(final int resultCode, final Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
}
