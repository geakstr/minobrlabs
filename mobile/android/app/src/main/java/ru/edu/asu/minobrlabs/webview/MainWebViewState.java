package ru.edu.asu.minobrlabs.webview;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class MainWebViewState implements Serializable {
    public final String os = "android";

    public String currentStatsChart = SensorTypes.MICROPHONE_DB.name;

    public final Map<String, Integer> charts = new HashMap<String, Integer>() {{
        put(SensorTypes.MICROPHONE_DB.name, -1);
        put(SensorTypes.ACCEL.name, -3);
        put(SensorTypes.GYRO.name, -3);
        put(SensorTypes.AIR_TEMPERATURE.name, -5);
        put(SensorTypes.HUMIDITY.name, -4);
        put(SensorTypes.ATMO_PRESSURE.name, -5);
        put(SensorTypes.LIGHT.name, -1);
        put(SensorTypes.SOLUTE_TEMPERATURE.name, -5);
        put(SensorTypes.VOLTAGE.name, -2);
        put(SensorTypes.AMPERAGE.name, -2);
        put(SensorTypes.PH.name, -1);
    }};

    public boolean isMainPage = true;
    public int intervalIdx = 1;
    public final long[] intervals = new long[]{40, 100, 250, 500, 1000, 60000};

    public long getCurrentInterval() {
        return intervals[intervalIdx];
    }

    public String getFormattedCurrentInterval() {
        final long interval = getCurrentInterval();
        final int divider = interval <= 1000 ? 1000 : 60000;
        final String metrics = divider == 1000 ? "сек" : "мин";

        return String.valueOf(divider / getCurrentInterval() + ("/" + metrics));
    }

    public long nextCurrentInterval() {
        if (++intervalIdx >= intervals.length) {
            intervalIdx = 0;
        }
        return intervals[intervalIdx];
    }

    public void setMainPage(final boolean flag) {
        this.isMainPage = flag;

        App.Preferences.writeMainWebViewState(this);
    }
}
