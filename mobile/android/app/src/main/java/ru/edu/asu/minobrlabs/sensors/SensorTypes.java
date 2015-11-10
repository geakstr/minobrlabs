package ru.edu.asu.minobrlabs.sensors;

import android.util.SparseArray;

public class SensorTypes {
    public static final int HUMIDITY = 1;
    public static final int AIR_TEMPERATURE = 2;
    public static final int LIGHT = 3;
    public static final int GYRO = 4;
    public static final int ACCEL = 5;
    public static final int MICROPHONE_DB = 6;

    public static final SparseArray<String> actions = new SparseArray<String>() {{
        put(HUMIDITY, "humidity");
        put(AIR_TEMPERATURE, "airTemperature");
        put(LIGHT, "light");
        put(GYRO, "gyro");
        put(ACCEL, "accel");
        put(MICROPHONE_DB, "microphone");
    }};
}
