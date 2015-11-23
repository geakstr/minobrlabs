package ru.edu.asu.minobrlabs.sensors;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;
import ru.edu.asu.minobrlabs.db.entities.params.Accel;
import ru.edu.asu.minobrlabs.db.entities.params.AtmoPressure;
import ru.edu.asu.minobrlabs.db.entities.params.AirTemperature;
import ru.edu.asu.minobrlabs.db.entities.params.Amperage;
import ru.edu.asu.minobrlabs.db.entities.params.Gyro;
import ru.edu.asu.minobrlabs.db.entities.params.Humidity;
import ru.edu.asu.minobrlabs.db.entities.params.Light;
import ru.edu.asu.minobrlabs.db.entities.params.Microphone;
import ru.edu.asu.minobrlabs.db.entities.params.Ph;
import ru.edu.asu.minobrlabs.db.entities.params.SoluteTemperature;
import ru.edu.asu.minobrlabs.db.entities.params.Voltage;

public enum SensorTypes {
    HUMIDITY("humidity", Humidity.class),
    AIR_TEMPERATURE("airTemperature", AirTemperature.class),
    LIGHT("light", Light.class),
    GYRO("gyro", Gyro.class),
    ACCEL("accel", Accel.class),
    ATMO_PRESSURE("atmoPressure", AtmoPressure.class),
    AMPERAGE("amperage", Amperage.class),
    PH("ph", Ph.class),
    SOLUTE_TEMPERATURE("soluteTemperature", SoluteTemperature.class),
    VOLTAGE("voltage", Voltage.class),
    MICROPHONE_DB("microphone", Microphone.class);

    private final String name;
    private final Class<? extends GenericParam> clazz;

    SensorTypes(final String name, final Class<? extends GenericParam> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<? extends GenericParam> getClazz() {
        return clazz;
    }
}
