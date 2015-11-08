package ru.edu.asu.minobrlabs.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    protected SensorManager localSensorsManager;
    protected Sensor localSensorLight;
    protected Sensor localSensorGyro;
    protected Sensor localSensorAccel;

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
        final int sensorType = event.sensor.getType();

        if (sensorType == Sensor.TYPE_LIGHT) {
            float lux = event.values[0];
            lux = lux < 1.0f ? 1.0f : lux;

            if (webViewLoaded) {
                webView.loadUrl(String.format("javascript:setLight('%s')", lux));
            }
        } else if (sensorType == Sensor.TYPE_GYROSCOPE || sensorType == Sensor.TYPE_ACCELEROMETER) {
            final float x = event.values[0];
            final float y = event.values[1];
            final float z = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

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
