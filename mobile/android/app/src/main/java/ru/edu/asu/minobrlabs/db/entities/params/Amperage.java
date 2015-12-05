package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Amperage extends GenericParam {
    public Amperage() {}

    public Amperage(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
