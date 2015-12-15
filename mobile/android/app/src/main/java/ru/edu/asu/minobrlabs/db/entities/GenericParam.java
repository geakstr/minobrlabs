package ru.edu.asu.minobrlabs.db.entities;

import java.io.Serializable;
import java.util.Date;

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
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class GenericParam implements Serializable {
    public Long _id;

    public String vals;
    public Long date;
    public Long experiment;

    public GenericParam() {}

    public GenericParam(final long date, final String vals, final long experiment) {
        this.vals = vals;
        this.date = date;
        this.experiment = experiment;
    }

    public static GenericParam createById(final int id, final long date, final String val, final long experimentId) {
        final SensorTypes.Type type = SensorTypes.byId(id);

        if (SensorTypes.HUMIDITY == type) {
            return new Humidity(date, val, experimentId);
        } else if (SensorTypes.AIR_TEMPERATURE == type) {
            return new AirTemperature(date, val, experimentId);
        } else if (SensorTypes.LIGHT == type) {
            return new Light(date, val, experimentId);
        } else if (SensorTypes.GYRO == type) {
            return new Gyro(date, val, experimentId);
        } else if (SensorTypes.ACCEL == type) {
            return new Accel(date, val, experimentId);
        } else if (SensorTypes.ATMO_PRESSURE == type) {
            return new AtmoPressure(date, val, experimentId);
        } else if (SensorTypes.AMPERAGE == type) {
            return new Amperage(date, val, experimentId);
        } else if (SensorTypes.PH == type) {
            return new Ph(date, val, experimentId);
        } else if (SensorTypes.SOLUTE_TEMPERATURE == type) {
            return new SoluteTemperature(date, val, experimentId);
        } else if (SensorTypes.VOLTAGE == type) {
            return new Voltage(date, val, experimentId);
        } else if (SensorTypes.MICROPHONE_DB == type) {
            return new Microphone(date, val, experimentId);
        }

        return null;
    }

    @Override
    public String toString() {
        final Class<?> clazz = getClass().getEnclosingClass();
        final String tag = clazz != null ? clazz.getName() : getClass().getName();

        return String.format(
                tag + " : [\n  _id : %s\n  vals : %s\n  date : %s\n  experiment : %s\n]",
                _id,
                vals,
                new Date(date).toString(),
                experiment
        );
    }
}
