package ru.edu.asu.minobrlabs.webview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;

public class WebViewCallback implements ISensorCallback {
    private final WebView webView;

    // Store prev accelerometer value for low pass filter
    private float[] accel;

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
                accel = lowPass(vals.clone(), accel, 0.025f);
                action = String.format("accel([%s, %s, %s])", accel[0], accel[1], accel[2]);
                break;
            case SensorTypes.MICROPHONE_DB:
                action = String.format("microphone(%s)", vals[0]);
                break;
            default:
                return;
        }
        webView.loadUrl(String.format("javascript:%s", action));
    }

    /**
     * Smooth sensor values
     */
    private float[] lowPass(final float[] input, final float[] output, final float alpha) {
        if (output == null) {
            return input;
        }
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }
}
