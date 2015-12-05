package ru.edu.asu.minobrlabs.sensors.local;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashMap;
import java.util.Map;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class LocalSensorsManager implements SensorEventListener {
    private final SensorManager sensorManager;

    private final Map<SensorTypes.Type, BuiltinSensorManager> sensorManagers;

    private final MicrophoneSensorManager microphoneSensorManager;
    private final LightSensorManager lightSensorManager;
    private final AccelSensorManager accelSensorManager;
    private final GyroSensorManager gyroSensorManager;

    public LocalSensorsManager() {
        this.sensorManager = (SensorManager) App.instance.getSystemService(Context.SENSOR_SERVICE);

        this.microphoneSensorManager = new MicrophoneSensorManager();
        this.lightSensorManager = new LightSensorManager(sensorManager);
        this.accelSensorManager = new AccelSensorManager(sensorManager);
        this.gyroSensorManager = new GyroSensorManager(sensorManager);

        this.sensorManagers = new HashMap<SensorTypes.Type, BuiltinSensorManager>() {{
            put(SensorTypes.MICROPHONE_DB, microphoneSensorManager);
            put(SensorTypes.LIGHT, lightSensorManager);
            put(SensorTypes.ACCEL, accelSensorManager);
            put(SensorTypes.GYRO, gyroSensorManager);
        }};
    }

    public void update() {
        microphoneSensorManager.update();
        lightSensorManager.update();
        accelSensorManager.update();
        gyroSensorManager.update();
    }

    public void registerListeners() {
        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();

        for (Map.Entry<SensorTypes.Type, BuiltinSensorManager> e : sensorManagers.entrySet()) {
            final SensorTypes.Type type = e.getKey();
            final BuiltinSensorManager sensor = e.getValue();
            if (sensor.available()) {
                if (null != sensor.getSensor()) {
                    sensorManager.registerListener(this, sensor.getSensor(), SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (state.charts.get(type.name) == -1) {
                    state.charts.put(type.name, 1);
                }
            } else {
                state.charts.put(type.name, -1);
            }
        }

        App.Preferences.writeMainWebViewState(state);

        App.state.storage.wantReInit = true;
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

        final int type = event.sensor.getType();
        if (SensorTypes.LIGHT.id == type) {
            lightSensorManager.setVal(val);
        } else if (SensorTypes.ACCEL.id == type) {
            accelSensorManager.setVal(val);
        } else if (SensorTypes.GYRO.id == type) {
            gyroSensorManager.setVal(val);
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

    }
}
