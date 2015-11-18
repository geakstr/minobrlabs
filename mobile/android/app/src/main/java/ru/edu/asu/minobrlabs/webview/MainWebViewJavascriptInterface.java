package ru.edu.asu.minobrlabs.webview;

import ru.edu.asu.minobrlabs.App;

public class MainWebViewJavascriptInterface {
    @android.webkit.JavascriptInterface
    public void updateState(final String chartName, final int chartState) {
        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        state.charts.put(chartName, chartState);
        App.Preferences.writeMainWebViewState(state);
    }
}
