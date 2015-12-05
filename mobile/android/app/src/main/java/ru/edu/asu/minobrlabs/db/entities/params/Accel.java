package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Accel extends GenericParam {
    public Accel() {}

    public Accel(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
