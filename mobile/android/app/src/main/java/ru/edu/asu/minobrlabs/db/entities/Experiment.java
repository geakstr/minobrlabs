package ru.edu.asu.minobrlabs.db.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Experiment implements Serializable {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

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
        return String.format("%s [%s]", name, dateFormat.format(new Date(date)));
    }

    public String fullToString() {
        return String.format(
                "Experiment : [\n  _id : %s\n  name : %s\n  date : %s\n]",
                _id,
                name,
                new Date(date).toString()
        );
    }
}
