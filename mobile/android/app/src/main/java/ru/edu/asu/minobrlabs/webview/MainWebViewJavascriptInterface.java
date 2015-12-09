package ru.edu.asu.minobrlabs.webview;

import android.webkit.JavascriptInterface;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.R;

public class MainWebViewJavascriptInterface {
    @JavascriptInterface
    public void updateState(final String chartName, final int chartState) {
        System.out.println(chartName);
        final MainWebViewState state = App.state.webViewState;
        state.charts.put(chartName, chartState);
        App.state.setWebViewState(state);
    }

    @JavascriptInterface
    public void selectStatsChart(final String chartName) {
        final MainWebViewState state = App.state.webViewState;
        state.currentStatsChart = chartName;
        App.state.setWebViewState(state);

        App.state.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.state.menu.findItem(R.id.action_experiment_interval).setTitle(state.getFormattedCurrentInterval());
            }
        });
    }
}
