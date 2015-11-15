package ru.edu.asu.minobrlabs.sensors.remote;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.Random;

import ru.edu.asu.minobrlabs.db.entities.params.AirTemperature;
import ru.edu.asu.minobrlabs.db.entities.params.Humidity;
import ru.edu.asu.minobrlabs.sensors.SensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class RemoteSensorsService extends IntentService {
    final Random rnd = new Random();

    public RemoteSensorsService() {
        super("remote-sensors-service");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        final Bundle bundle = new Bundle();
        if (rnd.nextBoolean()) {
            bundle.putSerializable(SensorCallback.bundleKey, new Humidity(new float[]{rnd.nextInt(100)}));
            bundle.putString(SensorCallback.bundleType, SensorTypes.HUMIDITY);
        } else {
            bundle.putSerializable(SensorCallback.bundleKey, new AirTemperature(new float[]{rnd.nextInt(100)}));
            bundle.putString(SensorCallback.bundleType, SensorTypes.AIR_TEMPERATURE);
        }
        receiver.send(Activity.RESULT_OK, bundle);
    }
}
