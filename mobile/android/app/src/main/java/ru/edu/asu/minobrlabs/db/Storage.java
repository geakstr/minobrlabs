package ru.edu.asu.minobrlabs.db;

import java.io.Serializable;
import java.util.Arrays;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.dao.Dao;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.db.entities.GenericParam;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class Storage implements Serializable {
    private boolean recording = false;
    private boolean wasRecording = false;

    public boolean wantReInit = false;
    public long sleepTime;

    private int idx;
    private final String[] updates;

    private final static int MAX_EXPERIMENT_SIZE = 1000000;
    private final int[] ids;
    private final long[] times;
    private final String[] vals;
    private int persistIdx;

    public Storage() {
        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();

        this.recording = false;
        this.wasRecording = false;

        this.idx = 0;
        this.updates = new String[255];
        this.sleepTime = state.getCurrentInterval();

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
        if (wasRecording) {
            final long experimentId = Dao.put(experiment);

            final GenericParam[] data = new GenericParam[persistIdx];
            for (int i = 0; i < persistIdx; i++) {
                data[i] = GenericParam.createById(ids[i], times[i], vals[i], experimentId);
            }
            if (persistIdx > 0) {
                Dao.put(data);
            }
            clear();
        }
    }

    public void push(final int id, final float[] val) {
        final long time = System.currentTimeMillis();
        final String strVal = Arrays.toString(val);

        if (recording) {
            persist(id, time, Arrays.toString(val));
        }

        if (idx >= 255) {
            idx = 0;
        }

        final String[] data = new String[3];
        data[0] = String.valueOf(id);
        data[1] = String.valueOf(time);
        data[2] = strVal;
        updates[idx++] = Arrays.toString(data);
    }

    public String updatesToString() {
        final int l = idx;

        idx = 0;

        return l == 0 ? null : Arrays.toString(Arrays.copyOf(updates, l));
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