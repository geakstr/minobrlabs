package ru.edu.asu.minobrlabs;

import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

import ru.edu.asu.minobrlabs.db.TemporaryStorage;
import ru.edu.asu.minobrlabs.sensors.AppSensorManager;
import ru.edu.asu.minobrlabs.sensors.SensorsState;

public class State {
    private Activity activity;
    private Menu menu;
    private WebView webView;

    private TemporaryStorage temporaryStorage;

    private AppSensorManager appSensorManager;

    private SensorsState sensorsState;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public TemporaryStorage getTemporaryStorage() {
        if (null == temporaryStorage) {
            temporaryStorage = new TemporaryStorage();
        }
        return temporaryStorage;
    }

    public AppSensorManager getAppSensorManager() {
        if (null == appSensorManager) {
            appSensorManager = new AppSensorManager();
        }
        return appSensorManager;
    }

    public SensorsState getSensorsState() {
        if (null == sensorsState) {
            sensorsState = new SensorsState();
        }
        return sensorsState;
    }
}
