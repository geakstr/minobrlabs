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
        for (int i = 0; i <= 100; i += 1) {
            final Message msg = handler.obtainMessage();
            final Bundle bundle = new Bundle();

            // TODO: Get data from detectors and put it here
            bundle.putString("cmd", String.format("javascript:setHumidity('%s');", i));

            msg.setData(bundle);
            handler.sendMessage(msg);

            try {
                sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
