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

    public final Map<String, Interval> intervals = new HashMap<String, Interval>() {{
        put(SensorTypes.MICROPHONE_DB.getName(), new Interval(300, 500, 1000));
        put(SensorTypes.ACCEL.getName(), new Interval(100, 200, 300, 500, 1000));
        put(SensorTypes.GYRO.getName(), new Interval(100, 200, 300, 500, 1000));
        put(SensorTypes.AIR_TEMPERATURE.getName(), new Interval(300, 500, 1000));
        put(SensorTypes.HUMIDITY.getName(), new Interval(200, 300, 500, 1000));
        put(SensorTypes.ATMO_PRESSURE.getName(), new Interval(100, 200, 300, 500, 1000));
        put(SensorTypes.LIGHT.getName(), new Interval(100, 200, 300, 500, 1000));
        put(SensorTypes.SOLUTE_TEMPERATURE.getName(), new Interval(100, 200, 300));
        put(SensorTypes.VOLTAGE.getName(), new Interval(100, 200, 300));
        put(SensorTypes.AMPERAGE.getName(), new Interval(100, 200, 300));
        put(SensorTypes.PH.getName(), new Interval(100, 200, 300));
    }};

    public int getCurrentInterval() {
        return intervals.get(currentStatsChart).cur();
    }

    public String getFormattedCurrentInterval() {
        return String.valueOf(getCurrentInterval() + " мс");
    }

    public int nextCurrentInterval() {
        return intervals.get(currentStatsChart).next();
    }

    public static class Interval {
        public int idx;
        public final int[] intervals;

        public Interval(final int... intervals) {
            this.intervals = intervals;
        }

        public int next() {
            if (++idx >= intervals.length) {
                idx = 0;
            }
            return cur();
        }

        public int cur() {
            return intervals[idx];
        }

        @Override
        public String toString() {
            return "Idx : " + idx + "; " + Arrays.toString(intervals);
        }
    }

    @Override
    public String toString() {
        return "Charts : \n" + charts.toString() + "\nIntervals : " + intervals.toString();
    }
}
