package ru.edu.asu.minobrlabs.sensors;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.util.Arrays;

import ru.edu.asu.minobrlabs.db.entities.Stat;

public class SensorCallback implements ISensorCallback {
    public static final String bundleKey = "stat";

    private final WebView webView;

    public SensorCallback(final WebView webView) {
        this.webView = webView;
    }

    @Override
    public void onReceiveResult(final int status, final Bundle bundle) {
        if (status != Activity.RESULT_OK) {
            return;
        }

        final Stat stat = (Stat) bundle.getSerializable(bundleKey);
        if (null == stat) {
            return;
        }

        final String action = SensorTypes.actions.get(stat.type);
        if (null == action) {
            return;
        }

        webView.loadUrl(String.format("javascript:%s(%s)", action, stat.vals));
    }
}