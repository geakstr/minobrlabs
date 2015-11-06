package ru.edu.asu.minobrlabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.edu.asu.minobrlabs.detectors.DetectorsReceiver;
import ru.edu.asu.minobrlabs.detectors.DetectorsThread;

public abstract class AbstractWebViewActivity extends AppCompatActivity {
    protected String webViewURL;
    protected WebView webView;
    protected DetectorsThread detectorsThread;
    protected DetectorsReceiver.Callback webViewCallback;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.detectorsThread = new DetectorsThread(getApplicationContext());
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

    protected void initWebView(final int viewId) {
        webView = (WebView) findViewById(viewId);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, final String url) {
                detectorsThread.start(webViewCallback);
            }
        });
        webView.loadUrl(webViewURL);
    }

    public void moveToStatistics() {
        final Intent i = new Intent(getApplicationContext(), StatsActivity.class);
        startActivity(i);
    }
}
