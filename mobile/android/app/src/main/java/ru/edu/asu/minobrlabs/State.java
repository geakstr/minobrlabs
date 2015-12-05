package ru.edu.asu.minobrlabs;

import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.db.Storage;
import ru.edu.asu.minobrlabs.sensors.AppSensorsManager;

public class State {
    public Activity activity;
    public Menu menu;
    public WebView webView;

    public final AppSensorsManager appSensorsManager;
    public final Storage storage;

    public State() {
        this.appSensorsManager = new AppSensorsManager();
        this.storage = new Storage();
    }
}
