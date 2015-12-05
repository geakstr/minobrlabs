package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class SoluteTemperature extends GenericParam {
    public SoluteTemperature() {}

    public SoluteTemperature(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
