package ru.edu.asu.minobrlabs.db.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import ru.edu.asu.minobrlabs.db.entities.Experiment;

public class GenericParam implements Serializable {
    public Long _id;

    public String vals;
    public Long date;
    public Experiment experiment;

    public GenericParam() {}

    public GenericParam(final String vals) {
        this.vals = vals;
        this.date = new Date().getTime();
        this.experiment = null;
    }

    public GenericParam(final float[] vals) {
        this(Arrays.toString(vals));
    }

    @Override
    public String toString() {
        final Class<?> clazz = getClass().getEnclosingClass();
        final String tag = clazz != null ? clazz.getName() : getClass().getName();

        return String.format(
                tag + " : [\n  _id : %s\n  vals : %s\n  date : %s\n  experiment : %s\n]",
                _id,
                vals,
                new Date(date).toString(),
                experiment
        );
    }
}
