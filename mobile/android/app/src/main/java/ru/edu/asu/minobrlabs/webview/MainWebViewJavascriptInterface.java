package ru.edu.asu.minobrlabs.webview;

import android.app.Activity;
import android.view.Menu;
import android.webkit.JavascriptInterface;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.local.LocalSensorsManager;

public class MainWebViewJavascriptInterface {
    private Activity activity;
    private Menu menu;
    private LocalSensorsManager localSensorsManager;

    public void setContext(final Activity activity, final Menu menu, final LocalSensorsManager localSensorsManager) {
        this.activity = activity;
        this.menu = menu;
        this.localSensorsManager = localSensorsManager;
    }

    @JavascriptInterface
    public void updateState(final String chartName, final int chartState) {
        System.out.println(chartName);
        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        state.charts.put(chartName, chartState);
        App.Preferences.writeMainWebViewState(state);
    }

    @JavascriptInterface
    public void selectStatsChart(final String chartName) {
        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        state.currentStatsChart = chartName;
        App.Preferences.writeMainWebViewState(state);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menu.findItem(R.id.action_experiment_interval).setTitle(state.getFormattedCurrentInterval());
            }
        });
    }
}
