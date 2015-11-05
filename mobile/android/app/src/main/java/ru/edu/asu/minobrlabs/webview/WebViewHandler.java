package ru.edu.asu.minobrlabs.webview;

import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

public class WebViewHandler extends Handler {
    private final WebView webView;

    public WebViewHandler(final WebView webView) {
        this.webView = webView;
    }

    @Override
    public void handleMessage(final Message msg) {
        final String cmd = msg.getData().getString("cmd");
        if (null != cmd) {
            webView.loadUrl(cmd);
        }
    }
}
