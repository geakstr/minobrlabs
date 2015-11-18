package ru.edu.asu.minobrlabs.activities;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;
import ru.edu.asu.minobrlabs.webview.WebViewPageFinishedCallback;

public class MainActivity extends AbstractActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initWebView("file:///android_asset/web/index.html", R.id.mainWebView, new WebViewPageFinishedCallback() {
            @Override
            public void callback(final WebView webView) {
                webView.loadUrl(String.format("javascript:init(%s)", App.Preferences.readMainWebViewStateAsJson()));
            }
        });
    }
}
