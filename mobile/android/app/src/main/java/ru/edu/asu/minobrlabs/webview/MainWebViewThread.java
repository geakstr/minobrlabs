package ru.edu.asu.minobrlabs.webview;

import android.os.Bundle;
import android.os.Message;
import android.webkit.WebView;

public class MainWebViewThread extends AbstractWebViewThread {
    public MainWebViewThread(final WebView mainWebView) {
        super(mainWebView);
    }

    @Override
    public void run() {
        final Message msg = handler.obtainMessage();
        final Bundle bundle = new Bundle();

        // TODO: Get data from detectors and put it here
        bundle.putString("cmd", String.format("javascript:setHumidity('%s');", 33.5));

        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
