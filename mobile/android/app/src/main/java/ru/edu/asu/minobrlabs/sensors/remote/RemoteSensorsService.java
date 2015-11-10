package ru.edu.asu.minobrlabs.sensors.remote;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.Random;

import ru.edu.asu.minobrlabs.db.entities.Stat;
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
        bundle.putSerializable("stat", new Stat(rnd.nextBoolean() ? SensorTypes.HUMIDITY : SensorTypes.AIR_TEMPERATURE, new float[]{rnd.nextInt(100)}));

        receiver.send(Activity.RESULT_OK, bundle);
    }
}
