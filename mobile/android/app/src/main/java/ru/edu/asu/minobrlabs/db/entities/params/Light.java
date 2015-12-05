package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Light extends GenericParam {
    public Light() {}

    public Light(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
