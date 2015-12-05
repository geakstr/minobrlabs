package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Humidity extends GenericParam {
    public Humidity() {}

    public Humidity(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
