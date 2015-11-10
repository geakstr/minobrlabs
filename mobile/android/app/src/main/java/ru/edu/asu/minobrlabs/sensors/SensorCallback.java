package ru.edu.asu.minobrlabs.sensors;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.db.entities.Stat;

public class SensorCallback implements ISensorCallback {
    private final WebView webView;

    public SensorCallback(final WebView webView) {
        this.webView = webView;
    }

    @Override
    public void onReceiveResult(final int status, final Bundle data) {
        if (status != Activity.RESULT_OK) {
            return;
        }

        final Stat stat = (Stat) data.getSerializable("stat");
        if (null == stat) {
            return;
        }

        String action;
        switch (stat.type) {
            case SensorTypes.HUMIDITY:
                action = "humidity";
                break;
            case SensorTypes.AIR_TEMPERATURE:
                action = "airTemperature";
                break;
            case SensorTypes.LIGHT:
                action = "light";
                break;
            case SensorTypes.GYRO:
                action = "gyro";
                break;
            case SensorTypes.ACCEL:
                action = "accel";
                break;
            case SensorTypes.MICROPHONE_DB:
                action = "microphone";
                break;
            default:
                return;
        }

        webView.loadUrl(String.format("javascript:%s(%s)", action, stat.vals));
    }
}