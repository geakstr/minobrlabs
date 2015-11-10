package ru.edu.asu.minobrlabs.webview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;

public class WebViewCallback implements ISensorCallback {
    private final WebView webView;

    public WebViewCallback(final WebView webView) {
        this.webView = webView;
    }

    public void onReceiveResult(final int status, final Bundle data) {
        if (status != Activity.RESULT_OK) {
            return;
        }

        final int type = data.getInt("type");
        final float[] vals = data.getFloatArray("values");
        if (0 == type || null == vals) {
            return;
        }

        String action;
        switch (type) {
            case SensorTypes.HUMIDITY:
                action = String.format("humidity(%s)", vals[0]);
                break;
            case SensorTypes.AIR_TEMPERATURE:
                action = String.format("airTemperature(%s)", vals[0]);
                break;
            case SensorTypes.LIGHT:
                float lux = vals[0];
                lux = lux < 1.0f ? 1.0f : lux;
                action = String.format("light(%s)", lux);
                break;
            case SensorTypes.GYRO:
                action = String.format("gyro([%s, %s, %s])", vals[0], vals[1], vals[2]);
                break;
            case SensorTypes.ACCEL:
                action = String.format("accel([%s, %s, %s])", vals[0], vals[1], vals[2]);
                break;
            case SensorTypes.MICROPHONE_DB:
                action = String.format("microphone(%s)", vals[0]);
                break;
            default:
                return;
        }
        webView.loadUrl(String.format("javascript:%s", action));
    }
}