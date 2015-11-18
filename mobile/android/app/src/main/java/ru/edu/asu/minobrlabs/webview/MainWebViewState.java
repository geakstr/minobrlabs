package ru.edu.asu.minobrlabs.webview;

import java.util.HashMap;
import java.util.Map;

import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class MainWebViewState {
    public final String os = "android";

    public final Map<String, Integer> charts = new HashMap<String, Integer>() {{
        put(SensorTypes.MICROPHONE_DB, 1);
        put(SensorTypes.ACCEL, 1);
        put(SensorTypes.GYRO, 1);
        put(SensorTypes.AIR_TEMPERATURE, 1);
        put(SensorTypes.HUMIDITY, 1);
        put(SensorTypes.AIR_PRESSURE, 1);
        put(SensorTypes.LIGHT, 1);
        put(SensorTypes.SOLUTE_TEMPERATURE, 1);
        put(SensorTypes.VOLTAGE, 1);
        put(SensorTypes.AMPERAGE, 1);
        put(SensorTypes.PH, 1);
    }};
}
