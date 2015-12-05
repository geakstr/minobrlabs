package ru.edu.asu.minobrlabs.db.entities.params;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Microphone extends GenericParam {
    public Microphone() {}

    public Microphone(final long date, final String vals, final long experimentId) {
        super(date, vals, experimentId);
    }
}
