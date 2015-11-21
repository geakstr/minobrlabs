package ru.edu.asu.minobrlabs.sensors;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.GenericParam;

public class SensorCallback implements ISensorCallback {
    public static final String bundleKey = "stat";
    public static final String bundleType = "type";

    private final WebView webView;

    public SensorCallback(final WebView webView) {
        this.webView = webView;
    }

    @Override
    public void onReceiveResult(final int status, final Bundle bundle) {
        if (status != Activity.RESULT_OK) {
            return;
        }

        final GenericParam stat = (GenericParam) bundle.getSerializable(bundleKey);
        if (null == stat) {
            return;
        }

        final SensorTypes sensorType = (SensorTypes) bundle.getSerializable(bundleType);
        if (null == sensorType) {
            return;
        }

        App.temporaryStorage().add(stat);

        webView.loadUrl(String.format("javascript:%s(%s, %s)", sensorType.getName(), stat.vals, stat.date));
    }
}