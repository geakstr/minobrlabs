package ru.edu.asu.minobrlabs.db.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class GenericStat implements Serializable {
    public Long _id;

    public String vals;
    public Long date;
    public TestName name;

    public GenericStat() {}

    public GenericStat(final String vals) {
        this.vals = vals;
        this.date = new Date().getTime();
        this.name = null;
    }

    public GenericStat(final float[] vals) {
        this(Arrays.toString(vals));
    }

    @Override
    public String toString() {
        String className = "";
        Class<?> enclosingClass = getClass().getEnclosingClass();
        if (enclosingClass != null) {
            className = enclosingClass.getName();
        } else {
            className = getClass().getName();
        }
        return String.format(
                className + " : [\n  _id : %s\n  vals : %s\n  date : %s\n  name : %s\n]",
                _id,
                vals,
                new Date(date).toString(),
                name
        );
    }
}
