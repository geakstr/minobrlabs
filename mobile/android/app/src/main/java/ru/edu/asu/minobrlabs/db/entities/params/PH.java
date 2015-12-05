package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Ph extends GenericParam {
    public Ph() {}

    public Ph(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
