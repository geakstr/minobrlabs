package ru.edu.asu.minobrlabs.db.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Experiment implements Serializable {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US);

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
}
