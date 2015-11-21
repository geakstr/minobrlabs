package ru.edu.asu.minobrlabs.db.entities;

import java.io.Serializable;
import java.util.Date;

public class Experiment implements Serializable {
    public Long _id;

    public String name;
    public Long date;

    public Experiment() {}

    public Experiment(final String name) {
        this.name = name;
        this.date = new Date().getTime();
    }

    @Override
    public String toString() {
        return String.format(
                "Experiment : [\n  _id : %s\n  name : %s\n]",
                _id,
                name
        );
    }
}
