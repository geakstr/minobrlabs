package ru.edu.asu.minobrlabs.db;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

import ru.edu.asu.minobrlabs.db.dao.Dao;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class Storage implements Serializable {
    private boolean recording = false;
    private boolean wasRecording = false;

    public boolean wantReInit = false;
    public long sleepTime;

    private int idx;
    private final String[] updates;


    public Storage() {
        this.recording = false;
        this.wasRecording = false;

        this.idx = 0;
        this.updates = new String[255];
        this.sleepTime = 40L;
    }

    public void startRecording() {
        recording = true;
        wasRecording = true;
    }

    public void stopRecording() {
        recording = false;
    }

    public void clear() {
        recording = false;
        wasRecording = false;
    }

    public void persist(final Experiment experiment) {
        if (wasRecording) {
            final long id = Dao.put(experiment);
//            for (final GenericParam genericParam : data) {
//                genericParam.experimentId = id;
//            }
            //Dao.put(data);
            clear();
        }
    }

    public void update(final int sensorId, final float[] val) {
        final String[] data = new String[3];
        data[0] = String.valueOf(sensorId);
        data[1] = String.valueOf(System.currentTimeMillis());
        data[2] = Arrays.toString(val);

        if (recording) {
            //data.add(stat);
        }

        updates[idx++] = Arrays.toString(data);
    }

    public String updatesToString() {
        final int l = idx;

        idx = 0;

        return l == 0 ? null : Arrays.toString(Arrays.copyOf(updates, l));
    }
}
