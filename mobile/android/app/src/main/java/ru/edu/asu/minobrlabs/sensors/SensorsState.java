package ru.edu.asu.minobrlabs.sensors;

import java.io.Serializable;
import java.util.LinkedList;

import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class SensorsState implements Serializable {
    public boolean wantReInit = false;

    private final LinkedList<Update> updates;

    public SensorsState() {
        this.updates = new LinkedList<>();
    }

    public void update(final SensorTypes type, final GenericParam param) {
        this.updates.add(new Update(type, param));
    }

    public static class Update {
        public final SensorTypes type;
        public final GenericParam param;

        public Update(final SensorTypes type, final GenericParam param) {
            this.type = type;
            this.param = param;
        }
    }

    public LinkedList<Update> getUpdates() {
        return updates;
    }
}
