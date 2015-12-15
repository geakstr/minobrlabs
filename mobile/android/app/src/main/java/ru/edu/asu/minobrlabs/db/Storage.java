package ru.edu.asu.minobrlabs.db;

import java.io.Serializable;
import java.util.Arrays;

import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Storage implements Serializable {
    private boolean recording = false;
    private boolean wasRecording = false;

    public boolean wantReInit = false;
    public long sleepTime;

    private int updateIdx;
    private final String[] updates;

    private final static int MAX_EXPERIMENT_SIZE = 1000000;
    private final int[] ids;
    private final long[] times;
    private final String[] vals;
    private int persistIdx;

    public Storage(final long sleepTime) {
        this.recording = false;
        this.wasRecording = false;

        this.sleepTime = sleepTime;

        this.updateIdx = 0;
        this.updates = new String[255];

        this.persistIdx = 0;
        this.ids = new int[MAX_EXPERIMENT_SIZE];
        this.times = new long[MAX_EXPERIMENT_SIZE];
        this.vals = new String[MAX_EXPERIMENT_SIZE];
    }

    public void startRecording() {
        recording = true;
        wasRecording = true;
    }

    public void stopRecording() {
        recording = false;
    }

    public void clear() {
        Arrays.fill(ids, 0);
        Arrays.fill(times, 0);
        Arrays.fill(vals, null);

        recording = false;
        wasRecording = false;
        persistIdx = 0;
    }

    public void persist(final Experiment experiment) {
        if (wasRecording && persistIdx > 0) {
            final long experimentId = Dao.put(experiment);
            final GenericParam[] data = new GenericParam[persistIdx];
            for (int i = 0; i < persistIdx; i++) {
                data[i] = GenericParam.createById(ids[i], times[i], vals[i], experimentId);
            }
            Dao.put(data);
            clear();
        }
    }

    public void push(final int id, final float[] val) {
        final long time = System.currentTimeMillis();
        final String strVal = Arrays.toString(val);

        if (recording) {
            persist(id, time, strVal);
        }

        if (updateIdx >= 255) {
            updateIdx = 0;
        }

        final String[] data = new String[3];
        data[0] = String.valueOf(id);
        data[1] = String.valueOf(time);
        data[2] = strVal;
        updates[updateIdx++] = Arrays.toString(data);
    }

    public void push(final String msg) {
        System.out.println(msg);
    }

    public String updatesToString() {
        final int l = updateIdx;

        updateIdx = 0;

        return l == 0 ? null : Arrays.toString(Arrays.copyOf(updates, l));
    }

    public void unexpectedPersist() {
        if (wasRecording) {
            persist(new Experiment("Автоматически сохраненный"));
        }
    }

    private void persist(final int id, final long time, final String val) {
        ids[persistIdx] = id;
        times[persistIdx] = time;
        vals[persistIdx] = val;

        persistIdx++;
        if (persistIdx == MAX_EXPERIMENT_SIZE) {
            persist(new Experiment("Автоматически сохраненный"));
            recording = true;
            wasRecording = true;
        }
    }
}
