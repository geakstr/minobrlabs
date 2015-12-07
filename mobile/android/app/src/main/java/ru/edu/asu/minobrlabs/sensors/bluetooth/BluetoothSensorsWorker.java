package ru.edu.asu.minobrlabs.sensors.bluetooth;

import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.AbstractSensorsWorker;

public class BluetoothSensorsWorker extends AbstractSensorsWorker {
    public static final String TAG = BluetoothSensorsWorker.class.getSimpleName();

    private BluetoothConnector.BluetoothSocketWrapper socket;

    public BluetoothSensorsWorker() {
        super();
    }

    @Override
    public void run() {
        try {
            if (null == socket || null == socket.getUnderlyingSocket() || !socket.getUnderlyingSocket().isConnected()) {
                socket = new BluetoothConnector("7C:C3:A1:51:31:64", true, null).connect();
            }

            int length = 1024;
            byte[] bytes = new byte[length];
            while (running) {
                length = socket.getInputStream().read(bytes);
                App.state.storage.push(new String(Arrays.copyOfRange(bytes, 0, length)));

                sleep(App.state.storage.sleepTime);
            }
        } catch (IOException e) {
            Log.e(TAG, "Bluetooth receiving data problems", e);
        } catch (InterruptedException e) {
            Log.e(TAG, "Bluetooth worker problems", e);
        }
    }

    @Override
    public void kill() {
        super.kill();

        try {
            socket.getOutputStream().close();
            socket.getInputStream().close();
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Bluetooth closing socket problems", e);
        }
    }
}
