package ru.edu.asu.minobrlabs;

import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.db.Storage;
import ru.edu.asu.minobrlabs.sensors.AppSensorsManager;
import ru.edu.asu.minobrlabs.sensors.AppSensorsThread;

public class State {
    public Activity activity;
    public Menu menu;
    public WebView webView;

    public final AppSensorsManager appSensorsManager;
    public final AppSensorsThread appSensorsThread;
    public final Storage storage;

    public State() {
        this.appSensorsManager = new AppSensorsManager();
        this.appSensorsThread = new AppSensorsThread();
        this.storage = new Storage();
    }
}
