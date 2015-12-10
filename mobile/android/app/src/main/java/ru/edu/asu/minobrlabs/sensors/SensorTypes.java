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
    public static final Type HUMIDITY = new Type(15, "humidity", Humidity.class, 'h');
    public static final Type AIR_TEMPERATURE = new Type(11, "airTemperature", AirTemperature.class, 't');
    public static final Type LIGHT = new Type(Sensor.TYPE_LIGHT, "light", Light.class, 'l');
    public static final Type GYRO = new Type(Sensor.TYPE_GYROSCOPE, "gyro", Gyro.class, 'g');
    public static final Type ACCEL = new Type(Sensor.TYPE_ACCELEROMETER, "accel", Accel.class, 'a');
    public static final Type ATMO_PRESSURE = new Type(14, "atmoPressure", AtmoPressure.class, 'r');
    public static final Type AMPERAGE = new Type(12, "amperage", Amperage.class, 'i');
    public static final Type PH = new Type(16, "ph", Ph.class, 'p');
    public static final Type SOLUTE_TEMPERATURE = new Type(17, "soluteTemperature", SoluteTemperature.class, 's');
    public static final Type VOLTAGE = new Type(13, "voltage", Voltage.class, 'v');
    public static final Type MICROPHONE_DB = new Type(18, "microphone", Microphone.class, 'm');

    public static final SensorTypes.Type[] values = new SensorTypes.Type[]{
            HUMIDITY, AIR_TEMPERATURE, LIGHT, GYRO, ACCEL, ATMO_PRESSURE, AMPERAGE, PH, SOLUTE_TEMPERATURE, VOLTAGE, MICROPHONE_DB
    };

    public static Type byId(final int id) {
        switch (id) {
            case 15:
                return HUMIDITY;
            case 11:
                return AIR_TEMPERATURE;
            case Sensor.TYPE_LIGHT:
                return LIGHT;
            case Sensor.TYPE_GYROSCOPE:
                return GYRO;
            case Sensor.TYPE_ACCELEROMETER:
                return ACCEL;
            case 14:
                return ATMO_PRESSURE;
            case 12:
                return AMPERAGE;
            case 16:
                return PH;
            case 17:
                return SOLUTE_TEMPERATURE;
            case 13:
                return VOLTAGE;
            case 18:
                return MICROPHONE_DB;
            default:
                return null;
        }
    }

    public static Type byChar(final char id) {
        switch (id) {
            case 'h':
                return HUMIDITY;
            case 't':
                return AIR_TEMPERATURE;
            case 'l':
                return LIGHT;
            case 'g':
                return GYRO;
            case 'a':
                return ACCEL;
            case 'r':
                return ATMO_PRESSURE;
            case 'i':
                return AMPERAGE;
            case 'p':
                return PH;
            case 's':
                return SOLUTE_TEMPERATURE;
            case 'v':
                return VOLTAGE;
            case 'm':
                return MICROPHONE_DB;
            default:
                return null;
        }
    }

    public static class Type {
        public final int id;
        public final String name;
        public final Class clazz;
        public final char letter;

        public Type(final int id, final String name, final Class clazz, final char letter) {
            this.id = id;
            this.name = name;
            this.clazz = clazz;
            this.letter = letter;
        }
    }
}
