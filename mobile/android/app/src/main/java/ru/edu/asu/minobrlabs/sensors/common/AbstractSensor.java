package ru.edu.asu.minobrlabs.sensors.common;

public abstract class AbstractSensor {
    public final float[] val;

    private final int size;

    public AbstractSensor(final int size) {
        this.size = size;
        this.val = new float[size];
    }

    public void setVal(final float[] val) {
        System.arraycopy(val, 0, this.val, 0, size);
    }

    public boolean update(final float val) {
        this.val[0] = val;

        return true;
    }

    public boolean update(final float[] val) {
        setVal(val);
        return true;
    }

    public boolean update() {
        return update(val);
    }
}
