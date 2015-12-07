package ru.edu.asu.minobrlabs;

import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.db.Storage;
import ru.edu.asu.minobrlabs.sensors.AppSensorsManager;
import ru.edu.asu.minobrlabs.sensors.local.LocalSensorsWorker;

public class State {
    public Activity activity;
    public Menu menu;
    public WebView webView;

    public final AppSensorsManager appSensorsManager;
    public final LocalSensorsWorker localSensorsWorker;
    public final Storage storage;

    public State() {
        this.appSensorsManager = new AppSensorsManager();
        this.localSensorsWorker = new LocalSensorsWorker();
        this.storage = new Storage();
    }
}
