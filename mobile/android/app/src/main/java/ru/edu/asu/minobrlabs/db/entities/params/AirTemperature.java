package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class AirTemperature extends GenericParam {
    public AirTemperature() {}

    public AirTemperature(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
