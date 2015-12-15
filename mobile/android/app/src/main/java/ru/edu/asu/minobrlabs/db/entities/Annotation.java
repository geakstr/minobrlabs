package ru.edu.asu.minobrlabs.db.entities;

import java.io.Serializable;

public class Annotation implements Serializable {
    public Long _id;

    public Long experiment;
    public Integer param;
    public Long time;
    public String text;

    public Annotation() {}

    public Annotation(final Long experiment, final Integer param, final Long time, final String text) {
        this.experiment = experiment;
        this.param = param;
        this.time = time;
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format(
                "Annotation : [\n  _id : %s\n  experiment : %s\n  param : %s\n  time : %s\n  text : %s\n]",
                _id, experiment, param, time, text
        );
    }
}
