package ru.edu.asu.minobrlabs.webview;

import android.webkit.WebView;

public class MainWebViewThread extends AbstractWebViewThread {
    public MainWebViewThread(final WebView mainWebView) {
        super(mainWebView);
    }

    @Override
    public void run() {
        // TODO: Get data from detectors and put it here

        for (int i = 0; i <= 100; i += 1) {
            handler.eval(String.format("setHumidity('%s');", i));

            try {
                sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
