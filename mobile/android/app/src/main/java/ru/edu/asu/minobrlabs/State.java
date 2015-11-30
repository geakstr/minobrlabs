package ru.edu.asu.minobrlabs;

import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.db.TemporaryStorage;
import ru.edu.asu.minobrlabs.sensors.AppSensorsManager;
import ru.edu.asu.minobrlabs.sensors.SensorsState;

public class State {
    public Activity activity;
    public Menu menu;
    public WebView webView;

    public final AppSensorsManager appSensorsManager;
    public final TemporaryStorage temporaryStorage;
    public final SensorsState sensors;

    public State() {
        this.appSensorsManager = new AppSensorsManager();
        this.temporaryStorage = new TemporaryStorage();
        this.sensors = new SensorsState();
    }
}
