package ru.edu.asu.minobrlabs;

import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.db.Storage;

public class State {
    public Activity activity;
    public Menu menu;
    public WebView webView;

    public final Storage storage;

    public State() {
        this.storage = new Storage();
    }
}
