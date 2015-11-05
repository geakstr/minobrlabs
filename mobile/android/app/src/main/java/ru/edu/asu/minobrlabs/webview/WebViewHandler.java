package ru.edu.asu.minobrlabs.webview;

import android.os.Bundle;
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
        final String js = msg.getData().getString("js");
        if (null != js) {
            webView.loadUrl(js);
        }
    }

    public void eval(final String js) {
        final Message msg = obtainMessage();
        final Bundle bundle = new Bundle();

        bundle.putString("js", String.format("javascript:%s", js));

        msg.setData(bundle);
        sendMessage(msg);
    }
}
