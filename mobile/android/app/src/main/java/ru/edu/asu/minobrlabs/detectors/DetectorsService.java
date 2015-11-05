package ru.edu.asu.minobrlabs.detectors;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.Random;

public class DetectorsService extends IntentService {
    final Random rnd = new Random();

    public DetectorsService() {
        super("detectors-service");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        final Bundle bundle = new Bundle();

        bundle.putString("param", rnd.nextBoolean() ? "humidity" : "temperature");
        bundle.putDouble("val", rnd.nextInt(100));

        receiver.send(Activity.RESULT_OK, bundle);
    }
}
