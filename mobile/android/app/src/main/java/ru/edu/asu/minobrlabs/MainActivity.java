package ru.edu.asu.minobrlabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.edu.asu.minobrlabs.detectors.DetectorsReceiver;
import ru.edu.asu.minobrlabs.detectors.DetectorsThread;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String mainWebViewHTML = "file:///android_asset/index.html";

    private DetectorsThread detectorsThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.detectorsThread = new DetectorsThread(getApplicationContext());

        initMainWebView();
    }

    private void initMainWebView() {
        final WebView mainWebView = (WebView) findViewById(R.id.mainWebView);

        mainWebView.getSettings().setJavaScriptEnabled(true);

        mainWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, final String url) {
                detectorsThread.start(new DetectorsReceiver.Callback() {
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
                        if ("humidity".equalsIgnoreCase(param)) {
                            action = "setHumidity";
                        } else if ("temperature".equalsIgnoreCase(param)) {
                            action = "setTemperature";
                        }

                        mainWebView.loadUrl(String.format("javascript:%s('%s')", action, val));
                    }
                });
            }
        });

        mainWebView.loadUrl(mainWebViewHTML);
    }
}
