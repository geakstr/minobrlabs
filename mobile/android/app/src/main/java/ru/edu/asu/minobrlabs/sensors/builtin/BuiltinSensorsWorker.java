package ru.edu.asu.minobrlabs.sensors.builtin;

import ru.edu.asu.minobrlabs.sensors.common.AbstractSensorsWorker;

public class BuiltinSensorsWorker extends AbstractSensorsWorker<BuiltinSensorsManager> {
    public static final String TAG = BuiltinSensorsWorker.class.getSimpleName();

    public BuiltinSensorsWorker(final BuiltinSensorsManager builtinSensorsManager) {
        super(builtinSensorsManager, TAG);
    }
}
