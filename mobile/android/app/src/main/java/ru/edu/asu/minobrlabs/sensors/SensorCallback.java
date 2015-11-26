package ru.edu.asu.minobrlabs.sensors;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.gson.Gson;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.GenericParam;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class SensorCallback implements ISensorCallback {
    public static final String bundleInitKey = "init";
    public static final String bundleInitState = "state";
    public static final String bundleStatKey = "stat";
    public static final String bundleStatType = "type";

    private final WebView webView;

    public SensorCallback(final WebView webView) {
        this.webView = webView;
    }

    @Override
    public void onReceiveResult(final int status, final Bundle bundle) {
        if (status != Activity.RESULT_OK) {
            return;
        }

        // If we want re-init webview state
        final boolean init = bundle.getBoolean(bundleInitKey, false);
        if (init) {
            final String state = bundle.getString(bundleInitState);
            if (null == state) {
                return;
            }

            webView.loadUrl(String.format("javascript:init(%s)", state));
            return;
        }

        // If we get new stat
        final GenericParam stat = (GenericParam) bundle.getSerializable(bundleStatKey);
        if (null == stat) {
            return;
        }

        final SensorTypes sensorType = (SensorTypes) bundle.getSerializable(bundleStatType);
        if (null == sensorType) {
            return;
        }

        App.temporaryStorage().add(stat);

        webView.loadUrl(String.format("javascript:%s(%s, %s)", sensorType.getName(), stat.vals, stat.date));
    }
}