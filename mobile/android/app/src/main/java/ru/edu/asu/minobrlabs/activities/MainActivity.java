package ru.edu.asu.minobrlabs.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import nl.qbusict.cupboard.QueryResultIterable;
import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.db.dao.Dao;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.db.entities.params.Accel;
import ru.edu.asu.minobrlabs.db.entities.params.AirTemperature;
import ru.edu.asu.minobrlabs.sensors.ISensorCallback;
import ru.edu.asu.minobrlabs.sensors.SensorCallback;
import ru.edu.asu.minobrlabs.sensors.local.LocalSensorsManager;
import ru.edu.asu.minobrlabs.sensors.remote.RemoteSensorsManager;
import ru.edu.asu.minobrlabs.webview.MainWebViewJavascriptInterface;
import ru.edu.asu.minobrlabs.webview.WebViewPageFinishedCallback;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Menu menu;

    private WebView webView;

    private LocalSensorsManager localSensorsManager;
    private RemoteSensorsManager remoteSensorsManager;

    private boolean wasOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wasOnPause = false;

        localSensorsManager = new LocalSensorsManager();
        remoteSensorsManager = new RemoteSensorsManager();

        webView = createWebView("file:///android_asset/web/index.html", R.id.mainWebView, new WebViewPageFinishedCallback() {
            @Override
            public void callback(final WebView webView) {
                final String state = App.Preferences.readMainWebViewStateAsJson();
                webView.loadUrl(String.format("javascript:init(%s)", state));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        localSensorsManager.registerListeners();

        if (wasOnPause) {
            localSensorsManager.start();
            remoteSensorsManager.start();

            wasOnPause = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        wasOnPause = true;

        localSensorsManager.unregisterListeners();
        localSensorsManager.stop();
        remoteSensorsManager.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        menu.findItem(R.id.action_clear_recording).setVisible(false);
        menu.findItem(R.id.action_persist_recording).setVisible(false);

        menu.findItem(R.id.action_start_recording).setVisible(true);
        menu.findItem(R.id.action_stop_recording).setVisible(false);

        menu.findItem(R.id.action_to_main).setVisible(false);
        menu.findItem(R.id.action_to_stats).setVisible(true);

        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_to_string_stat:
                final QueryResultIterable<Experiment> experiments = Dao.findAll(Experiment.class);
                final QueryResultIterable<Accel> accels = Dao.findAll(Accel.class);
                final QueryResultIterable<AirTemperature> airTemps = Dao.findAll(AirTemperature.class);
                for (Experiment experiment : experiments) {
                    System.out.println(experiment.toString());
                }
                for (Accel accel : accels) {
                    System.out.println(accel.toString());
                }
                for (AirTemperature airTemp : airTemps) {
                    System.out.println(airTemp.toString());
                }
                break;
            case R.id.action_to_stats:
                webView.loadUrl("javascript:showStatsPage()");
                menu.findItem(R.id.action_to_main).setVisible(true);
                menu.findItem(R.id.action_to_stats).setVisible(false);
                break;
            case R.id.action_to_main:
                webView.loadUrl("javascript:showMainPage()");
                menu.findItem(R.id.action_to_main).setVisible(false);
                menu.findItem(R.id.action_to_stats).setVisible(true);
                break;
            case R.id.action_start_recording:
                App.temporaryStorage().startRecording();
                webView.loadUrl("javascript:isRecording(true)");

                menu.findItem(R.id.action_start_recording).setVisible(false);
                menu.findItem(R.id.action_stop_recording).setVisible(true);
                menu.findItem(R.id.action_persist_recording).setVisible(false);
                menu.findItem(R.id.action_clear_recording).setVisible(false);
                break;
            case R.id.action_stop_recording:
                App.temporaryStorage().stopRecording();
                webView.loadUrl("javascript:isRecording(false)");

                menu.findItem(R.id.action_start_recording).setVisible(true);
                menu.findItem(R.id.action_stop_recording).setVisible(false);
                menu.findItem(R.id.action_persist_recording).setVisible(true);
                menu.findItem(R.id.action_clear_recording).setVisible(true);
                break;
            case R.id.action_persist_recording:
                createExperimentDialog().show();
                menu.findItem(R.id.action_persist_recording).setVisible(false);
                menu.findItem(R.id.action_clear_recording).setVisible(false);
                break;
            case R.id.action_clear_recording:
                App.temporaryStorage().clear();
                webView.loadUrl("javascript:clear()");

                menu.findItem(R.id.action_persist_recording).setVisible(false);
                menu.findItem(R.id.action_clear_recording).setVisible(false);
                break;
            default:
                break;
        }
        return true;
    }

    private AlertDialog.Builder createExperimentDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.experiment_name));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.temporaryStorage().persist(new Experiment(input.getText().toString().trim()));
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder;
    }

    private WebView createWebView(final String webViewURL, final int viewId, final WebViewPageFinishedCallback callback) {
        final WebView webView = (WebView) findViewById(viewId);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.addJavascriptInterface(new MainWebViewJavascriptInterface(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, final String url) {
                final ISensorCallback webViewCallback = new SensorCallback(webView);

                localSensorsManager.setCallback(webViewCallback);
                remoteSensorsManager.setCallback(webViewCallback);

                localSensorsManager.start();
                remoteSensorsManager.start();

                if (null != callback) {
                    callback.callback(webView);
                }
            }
        });
        webView.loadUrl(webViewURL);

        return webView;
    }
}
