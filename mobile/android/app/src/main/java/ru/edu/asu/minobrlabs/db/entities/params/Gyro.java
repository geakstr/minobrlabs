package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Gyro extends GenericParam {
    public Gyro() {}

    public Gyro(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
