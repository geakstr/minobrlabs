package ru.edu.asu.minobrlabs.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.db.dao.Dao;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;
import ru.edu.asu.minobrlabs.sensors.SensorsState;
import ru.edu.asu.minobrlabs.webview.MainWebViewJavascriptInterface;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class MainActivity extends AppCompatActivity {
    private boolean wasOnPause;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (App.state().getSensorsState().wantReInit) {
                App.state().getSensorsState().wantReInit = false;

                final String state = App.Preferences.readMainWebViewStateAsJson();
                App.state().getWebView().loadUrl(String.format("javascript:init(%s)", state));
                return;
            }

            final LinkedList<SensorsState.Update> updates = App.state().getSensorsState().getUpdates();
            while (!updates.isEmpty()) {
                final SensorsState.Update update = updates.poll();

                App.state().getTemporaryStorage().add(update.param);
                App.state().getWebView().loadUrl(String.format("javascript:%s(%s, %s)",
                                update.type.getName(),
                                update.param.vals,
                                update.param.date
                        )
                );
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.state().setActivity(this);
        App.state().setWebView(createWebView());

        wasOnPause = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (wasOnPause) {
            App.state().getAppSensorManager().init(broadcastReceiver);
        }

        wasOnPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        wasOnPause = true;

        App.state().getAppSensorManager().destroy(broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        App.state().setMenu(menu);

        final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
        menu.findItem(R.id.action_experiment_interval).setVisible(false);
        menu.findItem(R.id.action_experiment_interval).setTitle(state.getFormattedCurrentInterval());

        menu.findItem(R.id.action_experiments).setVisible(true);

        menu.findItem(R.id.action_repeat).setVisible(false);

        menu.findItem(R.id.action_clear_recording).setVisible(false);
        menu.findItem(R.id.action_persist_recording).setVisible(false);

        menu.findItem(R.id.action_start_recording).setVisible(true);
        menu.findItem(R.id.action_stop_recording).setVisible(false);

        menu.findItem(R.id.action_to_main).setVisible(false);
        menu.findItem(R.id.action_to_stats).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_experiments:
                createExperimentsListDialog().show();
                break;
            case R.id.action_repeat:
                App.state().getWebView().loadUrl("javascript:setRealtime()");
                App.state().getMenu().findItem(R.id.action_repeat).setVisible(false);
                App.state().getMenu().findItem(R.id.action_start_recording).setVisible(true);
                break;
            case R.id.action_to_stats:
                App.state().getWebView().loadUrl("javascript:showStatsPage()");
                App.state().getMenu().findItem(R.id.action_experiment_interval).setVisible(true);
                App.state().getMenu().findItem(R.id.action_to_main).setVisible(true);
                App.state().getMenu().findItem(R.id.action_to_stats).setVisible(false);
                break;
            case R.id.action_to_main:
                App.state().getWebView().loadUrl("javascript:showMainPage()");
                App.state().getMenu().findItem(R.id.action_experiment_interval).setVisible(false);
                App.state().getMenu().findItem(R.id.action_to_main).setVisible(false);
                App.state().getMenu().findItem(R.id.action_to_stats).setVisible(true);
                break;
            case R.id.action_start_recording:
                App.state().getTemporaryStorage().startRecording();
                App.state().getWebView().loadUrl("javascript:isRecording(true)");

                App.state().getMenu().findItem(R.id.action_experiments).setVisible(false);
                App.state().getMenu().findItem(R.id.action_start_recording).setVisible(false);
                App.state().getMenu().findItem(R.id.action_stop_recording).setVisible(true);
                App.state().getMenu().findItem(R.id.action_persist_recording).setVisible(false);
                App.state().getMenu().findItem(R.id.action_clear_recording).setVisible(false);
                break;
            case R.id.action_stop_recording:
                App.state().getTemporaryStorage().stopRecording();
                App.state().getWebView().loadUrl("javascript:isRecording(false)");

                App.state().getMenu().findItem(R.id.action_experiments).setVisible(false);
                App.state().getMenu().findItem(R.id.action_start_recording).setVisible(true);
                App.state().getMenu().findItem(R.id.action_stop_recording).setVisible(false);
                App.state().getMenu().findItem(R.id.action_persist_recording).setVisible(true);
                App.state().getMenu().findItem(R.id.action_clear_recording).setVisible(true);
                break;
            case R.id.action_persist_recording:
                createExperimentDialog().show();
                App.state().getMenu().findItem(R.id.action_experiments).setVisible(true);
                App.state().getMenu().findItem(R.id.action_persist_recording).setVisible(false);
                App.state().getMenu().findItem(R.id.action_clear_recording).setVisible(false);
                break;
            case R.id.action_clear_recording:
                App.state().getTemporaryStorage().clear();
                App.state().getWebView().loadUrl("javascript:clear()");

                App.state().getMenu().findItem(R.id.action_experiments).setVisible(true);
                App.state().getMenu().findItem(R.id.action_persist_recording).setVisible(false);
                App.state().getMenu().findItem(R.id.action_clear_recording).setVisible(false);
                break;
            case R.id.action_experiment_interval:
                final MainWebViewState state = App.Preferences.readMainWebViewStateAsObject();
                state.nextCurrentInterval();
                App.state().getMenu().findItem(R.id.action_experiment_interval).setTitle(state.getFormattedCurrentInterval());
                for (final SensorTypes type : SensorTypes.values()) {
                    if (type.getName().equalsIgnoreCase(state.currentStatsChart)) {
                        App.state().getAppSensorManager().getLocalSensorsManager().setSleepTime(type, state.getCurrentInterval());
                        break;
                    }
                }
                App.Preferences.writeMainWebViewState(state);
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
                App.state().getTemporaryStorage().persist(new Experiment(input.getText().toString().trim()));
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

    private AlertDialog.Builder createExperimentsListDialog() {
        final ArrayAdapter<Object> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        final List experiments = Dao.findAll(Experiment.class);
        for (final Object experiment : experiments) {
            adapter.add(experiment);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.experiments));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Experiment experiment = (Experiment) adapter.getItem(which);

                final String stats = new Gson().toJson(Dao.findByExperiment(experiment));

                App.state().getWebView().loadUrl(String.format("javascript:loadExperiment('%s', %s)", experiment.name, stats));

                App.state().getMenu().findItem(R.id.action_to_main).setVisible(true);
                App.state().getMenu().findItem(R.id.action_repeat).setVisible(true);
                App.state().getMenu().findItem(R.id.action_to_stats).setVisible(false);
                App.state().getMenu().findItem(R.id.action_start_recording).setVisible(false);
            }
        });
        return builder;
    }

    private WebView createWebView() {
        final WebView webView = (WebView) findViewById(R.id.mainWebView);

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
                App.state().getAppSensorManager().init(broadcastReceiver);

                final String state = App.Preferences.readMainWebViewStateAsJson();
                App.state().getWebView().loadUrl(String.format("javascript:init(%s)", state));
            }
        });
        webView.loadUrl("file:///android_asset/web/index.html");

        return webView;
    }
}
