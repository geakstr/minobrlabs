package ru.edu.asu.minobrlabs.webview;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class MainWebViewState implements Serializable {
    public final String os = "android";

    public String currentStatsChart = SensorTypes.MICROPHONE_DB.name;

    public final Map<String, Integer> charts = new HashMap<String, Integer>() {{
        put(SensorTypes.MICROPHONE_DB.name, -1);
        put(SensorTypes.ACCEL.name, -1);
        put(SensorTypes.GYRO.name, -1);
        put(SensorTypes.AIR_TEMPERATURE.name, -1);
        put(SensorTypes.HUMIDITY.name, -1);
        put(SensorTypes.ATMO_PRESSURE.name, -1);
        put(SensorTypes.LIGHT.name, -1);
        put(SensorTypes.SOLUTE_TEMPERATURE.name, -1);
        put(SensorTypes.VOLTAGE.name, -1);
        put(SensorTypes.AMPERAGE.name, -1);
        put(SensorTypes.PH.name, -1);
    }};

    public int intervalIdx = 1;
    public final long[] intervals = new long[]{40, 100, 1000, 60000};

    public long getCurrentInterval() {
        return intervals[intervalIdx];
    }

    public String getFormattedCurrentInterval() {
        return String.valueOf(1000 / getCurrentInterval() + "/сек");
    }

    public long nextCurrentInterval() {
        if (++intervalIdx >= intervals.length) {
            intervalIdx = 0;
        }
        return intervals[intervalIdx];
    }
}
