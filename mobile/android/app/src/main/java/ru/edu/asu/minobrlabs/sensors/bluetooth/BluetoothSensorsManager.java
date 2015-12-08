package ru.edu.asu.minobrlabs.sensors.bluetooth;

import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.common.ISensorsManager;

public class BluetoothSensorsManager implements ISensorsManager {
    public static final String TAG = BluetoothSensorsManager.class.getSimpleName();

    private BluetoothConnector.BluetoothSocketWrapper socket;

    private final byte[] bytes;

    public BluetoothSensorsManager() {
        this.bytes = new byte[1024];
    }

    @Override
    public void start() {
        if (null == socket || null == socket.getUnderlyingSocket() || !socket.getUnderlyingSocket().isConnected()) {
            socket = new BluetoothConnector("7C:C3:A1:51:31:64", true, null).connect();
        }
    }

    @Override
    public void update() {
        try {
            if (socket.getInputStream().available() > 0) {
                int size = socket.getInputStream().read(bytes);
                App.state.storage.push(new String(Arrays.copyOfRange(bytes, 0, size)));
            }
        } catch (IOException e) {
            Log.e(TAG, "Bluetooth receiving data problems", e);
        }
    }

    @Override
    public void kill() {
        try {
            if (null != socket) {
                socket.getOutputStream().close();
                socket.getInputStream().close();
                socket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Bluetooth killing data", e);
        }
    }
}
