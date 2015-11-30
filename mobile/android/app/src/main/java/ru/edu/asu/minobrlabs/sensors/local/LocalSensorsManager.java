package ru.edu.asu.minobrlabs.sensors.local;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.SensorsState;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class LocalSensorsManager implements SensorEventListener {
    private final SensorManager sensorManager;

    private final Map<SensorTypes, BuiltinSensorManager> sensorManagers;

    public LocalSensorsManager() {
        sensorManager = (SensorManager) App.getInstance().getSystemService(Context.SENSOR_SERVICE);

        sensorManagers = new HashMap<SensorTypes, BuiltinSensorManager>() {{
            put(SensorTypes.MICROPHONE_DB, new MicrophoneSensorManager());
            put(SensorTypes.LIGHT, new LightSensorManager(sensorManager));
            put(SensorTypes.ACCEL, new AccelSensorManager(sensorManager));
            put(SensorTypes.GYRO, new GyroSensorManager(sensorManager));
        }};
    }

    public final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (App.state().getSensorsState().wantReInit) {
                App.state().getSensorsState().wantReInit = false;

                final String state = App.Preferences.readMainWebViewStateAsJson();
                App.state().getWebView().loadUrl(String.format("javascript:init(%s)", state));
                return;
            }

            final LinkedList<SensorsState.Update> updates = App.state().getSensorsState().getUpdates();
            while (!updates.isEmpty()) {
                final SensorsState.Update update = updates.poll();

                App.state().getTemporaryStorage().add(update.param);
                App.state().getWebView().loadUrl(String.format("javascript:%s(%s, %s)",
                                update.type.getName(),
                                update.param.vals,
                                update.param.date
                        )
                );
            }
        }
    };

    public void update() {
        ((MicrophoneSensorManager) sensorManagers.get(SensorTypes.MICROPHONE_DB)).update();
    }

    public void setSleepTime(final SensorTypes type, final long time) {
        sensorManagers.get(type).setSleepTime(time);
    }

    public void registerListeners() {
        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();

        for (Map.Entry<SensorTypes, BuiltinSensorManager> e : sensorManagers.entrySet()) {
            final SensorTypes type = e.getKey();
            final BuiltinSensorManager sensor = e.getValue();
            if (sensor.available()) {
                if (null != sensor.getSensor()) {
                    sensorManager.registerListener(this, sensor.getSensor(), SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (state.charts.get(type.getName()) == -1) {
                    state.charts.put(type.getName(), 1);
                }
            } else {
                state.charts.put(type.getName(), -1);
            }
        }

        App.state().getSensorsState().wantReInit = true;
    }

    public void unregisterListeners() {
        ((MicrophoneSensorManager) sensorManagers.get(SensorTypes.MICROPHONE_DB)).unregisterListener();
        sensorManager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(final SensorEvent event) {
        final float[] val = event.values;
        if (null == val) {
            return;
        }
        for (final SensorTypes type : SensorTypes.values()) {
            if (type.getAndroidVal() == event.sensor.getType()) {
                sensorManagers.get(type).update(val);
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

    }
}
