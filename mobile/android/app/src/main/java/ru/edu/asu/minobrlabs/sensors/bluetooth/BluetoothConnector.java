package ru.edu.asu.minobrlabs.sensors.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothConnector {
    public static final String TAG = BluetoothConnector.class.getSimpleName();

    private String address;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothSocketWrapper bluetoothSocket;
    private boolean secure;
    private List<UUID> uuidCandidates;
    private int candidate;

    public BluetoothConnector(final String address, final BluetoothAdapter adapter, final boolean secure) {
        this.address = address;
        this.secure = secure;

        this.adapter = adapter;

        if (!isEnabled()) {
            enable();
        }
    }

    public void enable() {
        this.adapter.enable();
    }

    public boolean isEnabled() {
        return this.adapter.isEnabled();
    }

    public BluetoothSocketWrapper connect() throws IOException {
        boolean success = false;

        try {
            this.device = this.adapter.getRemoteDevice(address);

            if (this.uuidCandidates == null || this.uuidCandidates.isEmpty()) {
                this.uuidCandidates = new ArrayList<>();
                //this.uuidCandidates.add(device.getUuids()[0].getUuid());
                this.uuidCandidates.add(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
            }
            adapter.cancelDiscovery();
            while (selectSocket()) {
                try {
                    bluetoothSocket.connect();
                    success = true;
                    break;
                } catch (IOException e) {
                    try {
                        bluetoothSocket = new FallbackBluetoothSocket(bluetoothSocket.getUnderlyingSocket());
                        Thread.sleep(500);
                        bluetoothSocket.connect();
                        success = true;
                        break;
                    } catch (FallbackException e1) {
                        Log.e(TAG, "Could not initialize FallbackBluetoothSocket classes.", e1);
                    } catch (InterruptedException e1) {
                        Log.e(TAG, e1.getMessage(), e1);
                    } catch (IOException e1) {
                        Log.e(TAG, "Fallback failed. Cancelling.", e1);
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not select socket", e);
        }

        if (!success) {
            throw new IOException("Could not connect to device: " + device.getAddress());
        } else {
            Log.d(TAG, "Connected!");
        }

        return bluetoothSocket;
    }

    private boolean selectSocket() throws IOException {
        if (candidate >= uuidCandidates.size()) {
            return false;
        }

        BluetoothSocket tmp;
        UUID uuid = uuidCandidates.get(candidate++);

        Log.d(TAG, "Attempting to connect to UUID: " + uuid);
        if (secure) {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } else {
            tmp = device.createInsecureRfcommSocketToServiceRecord(uuid);
        }

        bluetoothSocket = new NativeBluetoothSocket(tmp);

        return true;
    }

    public interface BluetoothSocketWrapper {
        InputStream getInputStream() throws IOException;

        OutputStream getOutputStream() throws IOException;

        String getRemoteDeviceName();

        void connect() throws IOException;

        String getRemoteDeviceAddress();

        void close() throws IOException;

        BluetoothSocket getUnderlyingSocket();
    }

    public static class NativeBluetoothSocket implements BluetoothSocketWrapper {
        private BluetoothSocket socket;

        public NativeBluetoothSocket(BluetoothSocket tmp) {
            this.socket = tmp;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return socket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return socket.getOutputStream();
        }

        @Override
        public String getRemoteDeviceName() {
            return socket.getRemoteDevice().getName();
        }

        @Override
        public void connect() throws IOException {
            socket.connect();
        }

        @Override
        public String getRemoteDeviceAddress() {
            return socket.getRemoteDevice().getAddress();
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }

        @Override
        public BluetoothSocket getUnderlyingSocket() {
            return socket;
        }
    }

    public class FallbackBluetoothSocket extends NativeBluetoothSocket {
        private BluetoothSocket fallbackSocket;

        public FallbackBluetoothSocket(BluetoothSocket tmp) throws FallbackException {
            super(tmp);
            try {
                Class<?> clazz = tmp.getRemoteDevice().getClass();
                Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
                Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                Object[] params = new Object[]{1};
                fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
            } catch (Exception e) {
                throw new FallbackException(e);
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return fallbackSocket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return fallbackSocket.getOutputStream();
        }

        @Override
        public void connect() throws IOException {
            fallbackSocket.connect();
        }

        @Override
        public void close() throws IOException {
            fallbackSocket.close();
        }
    }

    public static class FallbackException extends Exception {
        private static final long serialVersionUID = 1L;

        public FallbackException(Exception e) {
            super(e);
        }
    }
}

