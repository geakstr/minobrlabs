package ru.edu.asu.minobrlabs.db.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class Stat implements Serializable {
    public Long _id;

    public Integer type;
    public String vals;
    public Long date;

    public Stat() {}

    public Stat(final int type, final String vals) {
        this.type = type;
        this.vals = vals;
        this.date = new Date().getTime();
    }

    public Stat(final int type, final float[] vals) {
        this(type, Arrays.toString(vals));
    }

    @Override
    public String toString() {
        return String.format(
                "Stat : [\n  _id : %s\n  type : %s\n  vals : %s\n  date : %s\n]",
                _id,
                type,
                vals,
                new Date(date).toString()
        );
    }
}
