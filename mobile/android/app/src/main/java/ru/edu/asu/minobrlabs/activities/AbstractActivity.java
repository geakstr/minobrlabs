package ru.edu.asu.minobrlabs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;
import ru.edu.asu.minobrlabs.sensors.local.LocalSensorsManager;
import ru.edu.asu.minobrlabs.sensors.remote.RemoteSensorsManager;
import ru.edu.asu.minobrlabs.webview.WebViewCallback;

public abstract class AbstractActivity extends AppCompatActivity {
    private static final String TAG = AbstractActivity.class.getSimpleName();

    private LocalSensorsManager localSensorsManager;
    private RemoteSensorsManager remoteSensorsManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.localSensorsManager = new LocalSensorsManager(getApplicationContext());
        this.remoteSensorsManager = new RemoteSensorsManager(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        localSensorsManager.registerListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // TODO: Думаю здесь также нужно что-то сделать с тредом в remoteSensorsManager
        localSensorsManager.unregisterListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_to_stats:
                moveToStatistics();
                break;
            default:
                break;
        }
        return true;
    }

    protected void initWebView(final String webViewURL, final int viewId) {
        final WebView webView = (WebView) findViewById(viewId);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, final String url) {
                final ISensorCallback webViewCallback = new WebViewCallback(webView);

                localSensorsManager.setCallback(webViewCallback);
                remoteSensorsManager.setCallback(webViewCallback);

                localSensorsManager.setRunning(true);
                remoteSensorsManager.setRunning(true);
            }
        });
        webView.loadUrl(webViewURL);
    }

    public void moveToStatistics() {
        final Intent i = new Intent(getApplicationContext(), StatsActivity.class);
        startActivity(i);
    }
}
