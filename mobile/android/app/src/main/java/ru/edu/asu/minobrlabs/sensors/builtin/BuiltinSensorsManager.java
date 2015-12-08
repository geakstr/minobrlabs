package ru.edu.asu.minobrlabs.sensors.builtin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashMap;
import java.util.Map;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.common.ISensorsManager;
import ru.edu.asu.minobrlabs.sensors.builtin.impl.AccelSensor;
import ru.edu.asu.minobrlabs.sensors.builtin.impl.GyroSensor;
import ru.edu.asu.minobrlabs.sensors.builtin.impl.LightSensor;
import ru.edu.asu.minobrlabs.sensors.builtin.impl.MicrophoneSensor;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class BuiltinSensorsManager implements SensorEventListener, ISensorsManager {
    private final SensorManager sensorManager;

    private final Map<SensorTypes.Type, BuiltinSensor> sensorManagers;

    private final MicrophoneSensor microphoneSensorManager;
    private final LightSensor lightSensorManager;
    private final AccelSensor accelSensorManager;
    private final GyroSensor gyroSensorManager;

    public BuiltinSensorsManager() {
        this.sensorManager = (SensorManager) App.instance.getSystemService(Context.SENSOR_SERVICE);

        this.microphoneSensorManager = new MicrophoneSensor();
        this.lightSensorManager = new LightSensor(sensorManager);
        this.accelSensorManager = new AccelSensor(sensorManager);
        this.gyroSensorManager = new GyroSensor(sensorManager);

        this.sensorManagers = new HashMap<SensorTypes.Type, BuiltinSensor>() {{
            put(SensorTypes.MICROPHONE_DB, microphoneSensorManager);
            put(SensorTypes.LIGHT, lightSensorManager);
            put(SensorTypes.ACCEL, accelSensorManager);
            put(SensorTypes.GYRO, gyroSensorManager);
        }};
    }

    @Override
    public void update() {
        microphoneSensorManager.update();
        lightSensorManager.update();
        accelSensorManager.update();
        gyroSensorManager.update();
    }

    @Override
    public void start() {
        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();

        for (Map.Entry<SensorTypes.Type, BuiltinSensor> e : sensorManagers.entrySet()) {
            final SensorTypes.Type type = e.getKey();
            final BuiltinSensor sensor = e.getValue();
            if (sensor.available()) {
                if (null != sensor.getSensor()) {
                    sensorManager.registerListener(this, sensor.getSensor(), SensorManager.SENSOR_DELAY_GAME);
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

    @Override
    public void kill() {
        ((MicrophoneSensor) sensorManagers.get(SensorTypes.MICROPHONE_DB)).unregisterListener();
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
