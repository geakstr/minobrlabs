package ru.edu.asu.minobrlabs.webview;

import android.webkit.WebView;

public class AbstractWebViewThread extends Thread implements Runnable{
    protected final WebViewHandler handler;

    public AbstractWebViewThread(final WebView webView) {
        handler = new WebViewHandler(webView);
    }
}
