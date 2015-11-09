package ru.edu.asu.minobrlabs.sensors;

import android.os.Bundle;

public interface ISensorCallback {
    void onReceiveResult(int status, Bundle data);
}
