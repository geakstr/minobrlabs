package ru.edu.asu.minobrlabs.webview;

import java.util.HashMap;
import java.util.Map;

import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class MainWebViewState {
    public final String os = "android";

    public final Map<String, Integer> charts = new HashMap<String, Integer>() {{
        put(SensorTypes.MICROPHONE_DB.getName(), 1);
        put(SensorTypes.ACCEL.getName(), 1);
        put(SensorTypes.GYRO.getName(), 1);
        put(SensorTypes.AIR_TEMPERATURE.getName(), 1);
        put(SensorTypes.HUMIDITY.getName(), 1);
        put(SensorTypes.ATMO_PRESSURE.getName(), 1);
        put(SensorTypes.LIGHT.getName(), 1);
        put(SensorTypes.SOLUTE_TEMPERATURE.getName(), 1);
        put(SensorTypes.VOLTAGE.getName(), 1);
        put(SensorTypes.AMPERAGE.getName(), 1);
        put(SensorTypes.PH.getName(), 1);
    }};
}
