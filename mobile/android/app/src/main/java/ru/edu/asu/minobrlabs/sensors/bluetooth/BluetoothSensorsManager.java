package ru.edu.asu.minobrlabs.sensors.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.bluetooth.impl.AirTemperatureSensor;
import ru.edu.asu.minobrlabs.sensors.bluetooth.impl.AmperageSensor;
import ru.edu.asu.minobrlabs.sensors.bluetooth.impl.AtmoPressureSensor;
import ru.edu.asu.minobrlabs.sensors.bluetooth.impl.HumiditySensor;
import ru.edu.asu.minobrlabs.sensors.bluetooth.impl.PhSensor;
import ru.edu.asu.minobrlabs.sensors.bluetooth.impl.SoluteTemperatureSensor;
import ru.edu.asu.minobrlabs.sensors.bluetooth.impl.VoltageSensor;
import ru.edu.asu.minobrlabs.sensors.common.AbstractSensor;
import ru.edu.asu.minobrlabs.sensors.common.ISensorsManager;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class BluetoothSensorsManager implements ISensorsManager {
    public static final String TAG = BluetoothSensorsManager.class.getSimpleName();

    private String address;
    private BluetoothConnector bluetooth;
    private BluetoothConnector.BluetoothSocketWrapper socket;

    private final Map<SensorTypes.Type, AbstractSensor> sensorManagers;
    private final HumiditySensor humiditySensor;
    private final AirTemperatureSensor airTemperatureSensor;
    private final AtmoPressureSensor atmoPressureSensor;
    private final AmperageSensor amperageSensor;
    private final PhSensor phSensor;
    private final SoluteTemperatureSensor soluteTemperatureSensor;
    private final VoltageSensor voltageSensor;

    private final byte[] bytes;
    private final StringBuilder sb;

    public BluetoothSensorsManager() {
        this.bytes = new byte[1024];

        this.address = null;

        this.humiditySensor = new HumiditySensor();
        this.airTemperatureSensor = new AirTemperatureSensor();
        this.atmoPressureSensor = new AtmoPressureSensor();
        this.amperageSensor = new AmperageSensor();
        this.phSensor = new PhSensor();
        this.soluteTemperatureSensor = new SoluteTemperatureSensor();
        this.voltageSensor = new VoltageSensor();

        this.sensorManagers = new HashMap<SensorTypes.Type, AbstractSensor>() {{
            put(SensorTypes.HUMIDITY, humiditySensor);
            put(SensorTypes.AIR_TEMPERATURE, airTemperatureSensor);
            put(SensorTypes.ATMO_PRESSURE, atmoPressureSensor);
            put(SensorTypes.AMPERAGE, amperageSensor);
            put(SensorTypes.PH, phSensor);
            put(SensorTypes.SOLUTE_TEMPERATURE, soluteTemperatureSensor);
            put(SensorTypes.VOLTAGE, voltageSensor);
        }};

        this.sb = new StringBuilder();
    }

    public static class BluetoothDeviceItem {
        public String name;
        public String address;

        public BluetoothDeviceItem(final String name, final String address) {
            this.name = name;
            this.address = address;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static List<BluetoothDeviceItem> getPairedDevices() {
        final List<BluetoothDeviceItem> ret = new ArrayList<>();
        for (final BluetoothDevice bt : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
            ret.add(new BluetoothDeviceItem(bt.getName(), bt.getAddress()));
        }
        return ret;
    }

    public void start(final String address) {
        if (checkSocket() && null != address && address.equalsIgnoreCase(this.address)) {
            return;
        }
        this.address = address;
        kill();
        start();
    }

    @Override
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null == address) {
                    deinit();
                    return;
                }

                final BluetoothAdapter adapter = enable();

                bluetooth = new BluetoothConnector(address, adapter, true);
                if (bluetooth.isEnabled()) {
                    connect();
                } else {
                    makeDisabledToast();
                    deinit();
                }
            }
        }).start();
    }

    @Override
    public void update() {
        try {
            if (checkSocket() && socket.getInputStream().available() > 0) {
                int size = socket.getInputStream().read(bytes);
                parse1(new String(Arrays.copyOfRange(bytes, 0, size)).toCharArray());
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

    public BluetoothAdapter enable() {
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if (null != adapter && !adapter.isEnabled()) {
            makeEnablingToast();
            int attempts = 0;
            while (!adapter.isEnabled()) {
                Log.d(TAG, "Bluetooth not enabled");

                adapter.enable();

                if (++attempts == 30) {
                    break;
                }

                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    Log.d(TAG, "Bluetooth was not enable", e);
                }
            }
            if (!adapter.isEnabled()) {
                makeDisabledToast();
            }
        }

        return adapter;
    }

    private void connect() {
        try {
            makeConnectingToast();
            socket = bluetooth.connect();
            init();
        } catch (IOException e) {
            deinit();
            makeUnsuccessfulToast();
        }
    }

    private void makeUnsuccessfulToast() {
        App.state.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.instance.getApplicationContext(), R.string.bt_unsuccessful, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void makeDisabledToast() {
        App.state.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.instance.getApplicationContext(), R.string.bt_offed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void makeEnablingToast() {
        App.state.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.instance.getApplicationContext(), R.string.bt_enabling, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void makeConnectingToast() {
        App.state.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.instance.getApplicationContext(), R.string.bt_connecting, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void init() {
        final MainWebViewState state = App.state.webViewState;

        for (final SensorTypes.Type type : sensorManagers.keySet()) {
            if (state.charts.get(type.name) < 0) {
                state.charts.put(type.name, Math.abs(state.charts.get(type.name)));
            }
        }

        App.state.setWebViewState(state);
        App.state.storage.wantReInit = true;
    }

    public void deinit() {
        final MainWebViewState state = App.state.webViewState;

        for (final SensorTypes.Type type : sensorManagers.keySet()) {
            state.charts.put(type.name, -(Math.abs(state.charts.get(type.name))));
        }

        App.state.setWebViewState(state);
        App.state.storage.wantReInit = true;
    }

    private void parse1(final char[] data) {
        try {
            final int l = data.length;
            int i, j;
            char k, v;
            for (i = 0; i < l; ) {
                k = data[i++];
                sb.setLength(0);
                for (j = i; j < l; j++, i++) {
                    v = data[j];
                    if (Character.isDigit(v) || v == '.') {
                        sb.append(v);
                    } else {
                        break;
                    }
                }
                switch (k) {
                    case 'h':
                        humiditySensor.update(Float.parseFloat(sb.toString()));
                        break;
                    case 't':
                        airTemperatureSensor.update(Float.parseFloat(sb.toString()));
                        break;
                    case 'r':
                        atmoPressureSensor.update(Float.parseFloat(sb.toString()));
                        break;
                    case 'i':
                        amperageSensor.update(Float.parseFloat(sb.toString()));
                        break;
                    case 'p':
                        phSensor.update(Float.parseFloat(sb.toString()));
                        break;
                    case 's':
                        soluteTemperatureSensor.update(Float.parseFloat(sb.toString()));
                        break;
                    case 'v':
                        voltageSensor.update(Float.parseFloat(sb.toString()));
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ignored) {

        }
    }

    private boolean checkSocket() {
        return socket != null && socket.getUnderlyingSocket() != null && socket.getUnderlyingSocket().isConnected();
    }
}
