package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Voltage extends GenericParam {
    public Voltage() {}

    public Voltage(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
