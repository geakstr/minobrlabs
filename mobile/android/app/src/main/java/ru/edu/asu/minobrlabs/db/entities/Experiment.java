package ru.edu.asu.minobrlabs.db.entities;

public class Experiment {
    public Long _id;

    public String name;

    public Experiment() {}

    public Experiment(final String name) {
        this.name = name;
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
