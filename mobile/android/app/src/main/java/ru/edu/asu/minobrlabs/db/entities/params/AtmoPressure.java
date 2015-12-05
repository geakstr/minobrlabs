package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class AtmoPressure extends GenericParam {
    public AtmoPressure() {}

    public AtmoPressure(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
