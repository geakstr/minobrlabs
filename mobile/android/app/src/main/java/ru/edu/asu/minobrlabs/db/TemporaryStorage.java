package ru.edu.asu.minobrlabs.db;

import java.util.ArrayList;
import java.util.List;

import ru.edu.asu.minobrlabs.db.dao.Dao;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class TemporaryStorage {
    private List<GenericParam> data;

    private boolean recording = false;
    private boolean wasRecording = false;

    public TemporaryStorage() {
        this.data = new ArrayList<>();
        this.recording = false;
        this.wasRecording = false;
    }

    public void startRecording() {
        recording = true;
        wasRecording = true;
    }

    public void stopRecording() {
        recording = false;
    }

    public void clear() {
        data.clear();
        recording = false;
        wasRecording = false;
    }

    public void persist(final Experiment experiment) {
        if (wasRecording) {
            final long id = Dao.put(experiment);
            for (final GenericParam genericParam : data) {
                genericParam.experimentId = id;
            }
            Dao.put(data);
            clear();
        }
    }

    public void add(final GenericParam stat) {
        if (recording) {
            data.add(stat);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (GenericParam genericParam : data) {
            sb.append(genericParam.toString()).append("\n");
        }

        return sb.toString();
    }
}
