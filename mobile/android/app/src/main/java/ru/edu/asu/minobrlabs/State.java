package ru.edu.asu.minobrlabs;

import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.db.Storage;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class State {
    public Activity activity;
    public Menu menu;
    public WebView webView;

    public final Storage storage;
    public MainWebViewState webViewState;

    public State() {
        this.webViewState = App.Preferences.readMainWebViewStateAsObject();
        this.storage = new Storage(this.webViewState.getCurrentInterval());
    }

    public void setWebViewState(final MainWebViewState state) {
        this.webViewState = state;
        App.Preferences.writeMainWebViewState(state);
    }
}
