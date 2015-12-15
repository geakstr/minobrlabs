package ru.edu.asu.minobrlabs.webview;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.webkit.JavascriptInterface;
import android.widget.EditText;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.db.Dao;
import ru.edu.asu.minobrlabs.db.entities.Annotation;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

public class MainWebViewJavascriptInterface {
    @JavascriptInterface
    public void updateState(final String chartName, final int chartState) {
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

    @JavascriptInterface
    public void setAnnotation(final String chartName, final long experiment, final long time, final String series) {
        App.state.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(App.state.activity);
                dialog.setTitle(App.state.activity.getString(R.string.annotation));
                final EditText input = new EditText(App.state.activity);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(input);
                dialog.setPositiveButton(App.state.activity.getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String text = input.getText().toString().trim();

                        App.state.webView.loadUrl(
                                String.format("javascript:setAnnotation('%s', %s, '%s', '%s')",
                                        chartName,
                                        time,
                                        text,
                                        series));

                        final Annotation annotation = new Annotation(experiment, SensorTypes.byName(chartName).id, time, text);
                        Dao.put(annotation);
                    }
                });
                dialog.setNegativeButton(App.state.activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    @JavascriptInterface
    public void removeAnnotation(final String chartName, final long experiment, final long time) {
        Dao.deleteAnnotation(new Annotation(experiment, SensorTypes.byName(chartName).id, time, null));
    }
}
