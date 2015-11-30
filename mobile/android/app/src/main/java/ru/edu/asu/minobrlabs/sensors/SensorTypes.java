package ru.edu.asu.minobrlabs.sensors;

import android.hardware.Sensor;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;
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

public enum SensorTypes {
    HUMIDITY("humidity", Humidity.class, -1),
    AIR_TEMPERATURE("airTemperature", AirTemperature.class, -1),
    LIGHT("light", Light.class, Sensor.TYPE_LIGHT),
    GYRO("gyro", Gyro.class, Sensor.TYPE_GYROSCOPE),
    ACCEL("accel", Accel.class, Sensor.TYPE_ACCELEROMETER),
    ATMO_PRESSURE("atmoPressure", AtmoPressure.class, -1),
    AMPERAGE("amperage", Amperage.class, -1),
    PH("ph", Ph.class, -1),
    SOLUTE_TEMPERATURE("soluteTemperature", SoluteTemperature.class, -1),
    VOLTAGE("voltage", Voltage.class, -1),
    MICROPHONE_DB("microphone", Microphone.class, -1);

    private final String name;
    private final Class<? extends GenericParam> clazz;
    private final int androidVal;

    SensorTypes(final String name, final Class<? extends GenericParam> clazz, final int androidVal) {
        this.name = name;
        this.clazz = clazz;
        this.androidVal = androidVal;
    }

    public String getName() {
        return name;
    }

    public Class<? extends GenericParam> getClazz() {
        return clazz;
    }

    public int getAndroidVal() {
        return androidVal;
    }
}
