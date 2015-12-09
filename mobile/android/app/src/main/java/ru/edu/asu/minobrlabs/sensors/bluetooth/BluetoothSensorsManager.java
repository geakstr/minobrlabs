package ru.edu.asu.minobrlabs.sensors.bluetooth;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.sensors.common.ISensorsManager;

public class BluetoothSensorsManager implements ISensorsManager {
    public static final String TAG = BluetoothSensorsManager.class.getSimpleName();

    private BluetoothConnector bluetooth;
    private BluetoothConnector.BluetoothSocketWrapper socket;

    private final byte[] bytes;

    public BluetoothSensorsManager() {
        this.bytes = new byte[1024];
    }

    @Override
    public void start() {
        if (null == socket || null == socket.getUnderlyingSocket() || !socket.getUnderlyingSocket().isConnected()) {
            bluetooth = new BluetoothConnector("7C:C3:A1:51:31:64", true);
            if (!bluetooth.isEnabled()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int attempts = 0;

                        while (!bluetooth.isEnabled()) {
                            Log.d(TAG, "Bluetooth not enabled");

                            bluetooth.enable();

                            if (++attempts == 30) {

                                break;
                            }
                            try {
                                Thread.sleep(500L);
                            } catch (InterruptedException ignored) {

                            }
                        }

                        if (bluetooth.isEnabled()) {
                            socket = bluetooth.connect();
                        } else {
                            App.state.activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(App.instance.getApplicationContext(), R.string.bt_offed, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            } else {
                socket = bluetooth.connect();
            }
        }
    }

    @Override
    public void update() {
        try {
            if (socket != null && socket.getUnderlyingSocket() != null && socket.getUnderlyingSocket().isConnected() && socket.getInputStream().available() > 0) {
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
