package ru.edu.asu.minobrlabs.db.entities;

public class TestName {
    public Long _id;

    public String name;

    public TestName() {}

    public TestName(final String name) {
        this.name = name;
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
                className + " : [\n  _id : %s\n  name : %s\n]",
                _id,
                name
        );
    }
}
