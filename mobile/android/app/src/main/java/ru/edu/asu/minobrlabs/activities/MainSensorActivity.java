package ru.edu.asu.minobrlabs.activities;

import android.os.Bundle;

import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.sensors.RemoteSensorsReceiver;

public class MainSensorActivity extends AbstractSensorActivity {
    private static final String TAG = MainSensorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.webViewURL = "file:///android_asset/web/index.html";
        this.webViewCallback = new RemoteSensorsReceiver.Callback() {
            @Override
            public void onReceiveResult(final int status, final Bundle data) {
                if (status != RESULT_OK) {
                    return;
                }

                final String param = data.getString("param");
                final Double val = data.getDouble("val");
                if (null == param) {
                    return;
                }

                String action = "";
                boolean wasAction = false;
                if ("humidity".equalsIgnoreCase(param)) {
                    action = "setHumidity";
                    wasAction = true;
                } else if ("temperature".equalsIgnoreCase(param)) {
                    action = "setTemperature";
                    wasAction = true;
                }

                if (wasAction) {
                    webView.loadUrl(String.format("javascript:%s('%s')", action, val));
                }
            }
        };
        this.initWebView(R.id.mainWebView);
    }
}
