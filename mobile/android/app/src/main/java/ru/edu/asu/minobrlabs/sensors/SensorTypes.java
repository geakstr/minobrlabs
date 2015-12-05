package ru.edu.asu.minobrlabs.sensors;

import android.hardware.Sensor;

import ru.edu.asu.minobrlabs.db.entities.params.Accel;
import ru.edu.asu.minobrlabs.db.entities.params.AirTemperature;
import ru.edu.asu.minobrlabs.db.entities.params.Amperage;
import ru.edu.asu.minobrlabs.db.entities.params.AtmoPressure;
import ru.edu.asu.minobrlabs.db.entities.params.Gyro;
import ru.edu.asu.minobrlabs.db.entities.params.Humidity;
import ru.edu.asu.minobrlabs.db.entities.params.Light;
import ru.edu.asu.minobrlabs.db.entities.params.Microphone;
import ru.edu.asu.minobrlabs.db.entities.params.Ph;
import ru.edu.asu.minobrlabs.db.entities.params.SoluteTemperature;
import ru.edu.asu.minobrlabs.db.entities.params.Voltage;

public class SensorTypes {
    public static final Type HUMIDITY = new Type(15, "humidity", Humidity.class);
    public static final Type AIR_TEMPERATURE = new Type(11, "airTemperature", AirTemperature.class);
    public static final Type LIGHT = new Type(Sensor.TYPE_LIGHT, "light", Light.class);
    public static final Type GYRO = new Type(Sensor.TYPE_GYROSCOPE, "gyro", Gyro.class);
    public static final Type ACCEL = new Type(Sensor.TYPE_ACCELEROMETER, "accel", Accel.class);
    public static final Type ATMO_PRESSURE = new Type(14, "atmoPressure", AtmoPressure.class);
    public static final Type AMPERAGE = new Type(12, "amperage", Amperage.class);
    public static final Type PH = new Type(16, "ph", Ph.class);
    public static final Type SOLUTE_TEMPERATURE = new Type(17, "soluteTemperature", SoluteTemperature.class);
    public static final Type VOLTAGE = new Type(13, "voltage", Voltage.class);
    public static final Type MICROPHONE_DB = new Type(18, "microphone", Microphone.class);

    public static final SensorTypes.Type[] values = new SensorTypes.Type[]{
            HUMIDITY, AIR_TEMPERATURE, LIGHT, GYRO, ACCEL, ATMO_PRESSURE, AMPERAGE, PH, SOLUTE_TEMPERATURE, VOLTAGE, MICROPHONE_DB
    };

    public static class Type {
        public final int id;
        public final String name;
        public final Class clazz;

        public Type(final int id, final String name, final Class clazz) {
            this.id = id;
            this.name = name;
            this.clazz = clazz;
        }
    }
}
