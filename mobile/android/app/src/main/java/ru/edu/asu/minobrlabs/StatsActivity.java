package ru.edu.asu.minobrlabs;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import ru.edu.asu.minobrlabs.detectors.DetectorsReceiver;

public class StatsActivity extends AbstractWebViewActivity {
    private static final String TAG = StatsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        this.webViewURL = "file:///android_asset/web/stats.html";
        this.webViewCallback = new DetectorsReceiver.Callback() {
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
                if ("temperature".equalsIgnoreCase(param)) {
                    action = "addTemperature";
                    wasAction = true;
                }

                if (wasAction) {
                    webView.loadUrl(String.format("javascript:%s('%s')", action, val));
                }
            }
        };
        this.initWebView(R.id.statsWebView);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_to_stats).setVisible(false);
        return true;
    }
}