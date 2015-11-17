package ru.edu.asu.minobrlabs.activities;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.webview.WebViewPageFinishedCallback;

public class MainActivity extends AbstractActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initWebView("file:///android_asset/web/index.html", R.id.mainWebView, new WebViewPageFinishedCallback() {
            @Override
            public void callback(final WebView webView) {
                try {
                    final JSONObject config = new JSONObject();

                    config.put("os", "android");

                    final JSONObject charts = new JSONObject();
                    charts.put("microphone", 2);
                    charts.put("accel", 3);
                    charts.put("gyro", 3);
                    charts.put("airTemperature", 4);
                    charts.put("humidity", 1);
                    charts.put("atmoPressure", 0);
                    charts.put("light", 2);
                    charts.put("soluteTemperature", 1);
                    charts.put("voltage", 5);
                    charts.put("amperage", 1);
                    charts.put("ph", 1);

                    config.put("charts", charts);

                    webView.loadUrl(String.format("javascript:init(%s)", config.toString()));
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
    }
}
