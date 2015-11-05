package ru.edu.asu.minobrlabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.edu.asu.minobrlabs.webview.MainWebViewThread;

public class MainActivity extends AppCompatActivity {
    private static final String mainWebViewHTML = "file:///android_asset/index.html";

    // TODO: Read about stopping threads between activity
    private MainWebViewThread mainWebViewThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainWebView();
    }

    private void initMainWebView() {
        final WebView mainWebView = (WebView) findViewById(R.id.mainWebView);

        mainWebView.getSettings().setJavaScriptEnabled(true);

        mainWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, final String url) {
                mainWebViewThread.start();
            }
        });

        mainWebViewThread = new MainWebViewThread(mainWebView);
        mainWebView.loadUrl(mainWebViewHTML);
    }
}
