package ru.edu.asu.minobrlabs.webview;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class MainWebViewState implements Serializable {
    public final String os = "android";

    public String currentStatsChart = SensorTypes.MICROPHONE_DB.getName();

    public final Map<String, Integer> charts = new HashMap<String, Integer>() {{
        put(SensorTypes.MICROPHONE_DB.getName(), -1);
        put(SensorTypes.ACCEL.getName(), -1);
        put(SensorTypes.GYRO.getName(), -1);
        put(SensorTypes.AIR_TEMPERATURE.getName(), -1);
        put(SensorTypes.HUMIDITY.getName(), -1);
        put(SensorTypes.ATMO_PRESSURE.getName(), -1);
        put(SensorTypes.LIGHT.getName(), -1);
        put(SensorTypes.SOLUTE_TEMPERATURE.getName(), -1);
        put(SensorTypes.VOLTAGE.getName(), -1);
        put(SensorTypes.AMPERAGE.getName(), -1);
        put(SensorTypes.PH.getName(), -1);
    }};

    public int intervalIdx = 1;
    public final long[] intervals = new long[]{40, 100, 1000, 60000};

    public long getCurrentInterval() {
        return intervals[intervalIdx];
    }

    public String getFormattedCurrentInterval() {
        return String.valueOf(getCurrentInterval() + " мс");
    }

    public long nextCurrentInterval() {
        if (++intervalIdx >= intervals.length) {
            intervalIdx = 0;
        }
        return intervals[intervalIdx];
    }
}
