package ru.edu.asu.minobrlabs.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Arrays;

import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.sensors.RemoteSensorsReceiver;
import ru.edu.asu.minobrlabs.sensors.RemoteSensorsThread;

public abstract class AbstractSensorActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = AbstractSensorActivity.class.getSimpleName();

    protected WebView webView;
    protected String webViewURL;
    protected boolean webViewLoaded;
    protected RemoteSensorsThread remoteSensorsThread;
    protected RemoteSensorsReceiver.Callback webViewCallback;

    private SensorManager localSensorsManager;
    private Sensor localSensorLight;
    private Sensor localSensorGyro;
    private Sensor localSensorAccel;

    private static final float ALPHA = 0.025f;

    // Store prev accelerometer value for low pass filter
    private float[] accelValues;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.remoteSensorsThread = new RemoteSensorsThread(getApplicationContext());

        this.webViewLoaded = false;

        this.localSensorsManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.localSensorLight = localSensorsManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        this.localSensorGyro = localSensorsManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        this.localSensorAccel = localSensorsManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != localSensorLight) {
            localSensorsManager.registerListener(this, localSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (null != localSensorGyro) {
            localSensorsManager.registerListener(this, localSensorGyro, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (null != localSensorAccel) {
            localSensorsManager.registerListener(this, localSensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        localSensorsManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_to_stats:
                moveToStatistics();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public final void onSensorChanged(final SensorEvent event) {
        if (!webViewLoaded) {
            return;
        }

        final int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_LIGHT) {
            float lux = event.values[0];
            lux = lux < 1.0f ? 1.0f : lux;

            webView.loadUrl(String.format("javascript:setLight('%s')", lux));
        } else if (sensorType == Sensor.TYPE_GYROSCOPE) {
            webView.loadUrl(String.format("javascript:setGyro([%s, %s, %s])", event.values[0], event.values[1], event.values[2]));
        } else if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            accelValues = lowPass(event.values.clone(), accelValues);
            webView.loadUrl(String.format("javascript:setAccel([%s, %s, %s])", accelValues[0], accelValues[1], accelValues[2]));
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

    }

    /**
     * Smooth sensor values
     */
    private float[] lowPass(final float[] input, final float[] output) {
        if (output == null) {
            return input;
        }
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    protected void initWebView(final int viewId) {
        webView = (WebView) findViewById(viewId);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, final String url) {
                webViewLoaded = true;

                remoteSensorsThread.start(webViewCallback);
            }
        });
        webView.loadUrl(webViewURL);
    }

    public void moveToStatistics() {
        final Intent i = new Intent(getApplicationContext(), StatsSensorActivity.class);
        startActivity(i);
    }
}
