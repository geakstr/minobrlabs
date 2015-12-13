package ru.edu.asu.minobrlabs.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.List;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.R;
import ru.edu.asu.minobrlabs.db.dao.Dao;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.sensors.AppSensorsWorker;
import ru.edu.asu.minobrlabs.sensors.bluetooth.BluetoothSensorsManager;
import ru.edu.asu.minobrlabs.sensors.builtin.BuiltinSensorsManager;
import ru.edu.asu.minobrlabs.webview.MainWebViewJavascriptInterface;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private boolean pause;

    private BuiltinSensorsManager builtinSensors;
    private BluetoothSensorsManager bluetoothSensors;
    private AppSensorsWorker appSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");
        pause = false;

        App.state.activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume()");

        if (!pause) {
            builtinSensors = new BuiltinSensorsManager();
            bluetoothSensors = new BluetoothSensorsManager();
            appSensors = new AppSensorsWorker(builtinSensors, bluetoothSensors);
            App.state.webView = createWebView();
        }

        pause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause()");

        pause = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop()");

        exit();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        App.state.menu = menu;

        initMenu(menu);

        menu.findItem(R.id.action_to_main).setVisible(!App.state.webViewState.isMainPage);
        menu.findItem(R.id.action_to_stats).setVisible(App.state.webViewState.isMainPage);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_devices:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothSensors.enable();
                        App.state.activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createBluetoothDevicesDialog().show();
                            }
                        });
                    }
                }).start();
                break;
            case R.id.action_experiments:
                createExperimentsListDialog().show();
                break;
            case R.id.action_repeat:
                App.state.webView.loadUrl("javascript:setRealtime()");
                App.state.menu.findItem(R.id.action_repeat).setVisible(false);
                App.state.menu.findItem(R.id.action_start_recording).setVisible(true);
                break;
            case R.id.action_to_stats:
                App.state.webViewState.setMainPage(false);
                App.state.webView.loadUrl("javascript:showStatsPage()");
                App.state.menu.findItem(R.id.action_experiment_interval).setVisible(true);
                App.state.menu.findItem(R.id.action_to_main).setVisible(true);
                App.state.menu.findItem(R.id.action_to_stats).setVisible(false);
                break;
            case R.id.action_to_main:
                App.state.webViewState.setMainPage(true);
                App.state.webView.loadUrl("javascript:showMainPage()");
                App.state.menu.findItem(R.id.action_experiment_interval).setVisible(true);
                App.state.menu.findItem(R.id.action_to_main).setVisible(false);
                App.state.menu.findItem(R.id.action_to_stats).setVisible(true);
                break;
            case R.id.action_start_recording:
                App.state.storage.startRecording();
                App.state.webView.loadUrl("javascript:isRecording(true)");

                App.state.menu.findItem(R.id.action_experiments).setVisible(false);
                App.state.menu.findItem(R.id.action_start_recording).setVisible(false);
                App.state.menu.findItem(R.id.action_stop_recording).setVisible(true);
                App.state.menu.findItem(R.id.action_persist_recording).setVisible(false);
                App.state.menu.findItem(R.id.action_clear_recording).setVisible(false);
                break;
            case R.id.action_stop_recording:
                App.state.storage.stopRecording();
                App.state.webView.loadUrl("javascript:isRecording(false)");

                App.state.menu.findItem(R.id.action_experiments).setVisible(false);
                App.state.menu.findItem(R.id.action_start_recording).setVisible(true);
                App.state.menu.findItem(R.id.action_stop_recording).setVisible(false);
                App.state.menu.findItem(R.id.action_persist_recording).setVisible(true);
                App.state.menu.findItem(R.id.action_clear_recording).setVisible(true);
                break;
            case R.id.action_persist_recording:
                createExperimentDialog().show();
                App.state.menu.findItem(R.id.action_experiments).setVisible(true);
                App.state.menu.findItem(R.id.action_persist_recording).setVisible(false);
                App.state.menu.findItem(R.id.action_clear_recording).setVisible(false);
                break;
            case R.id.action_clear_recording:
                App.state.storage.clear();
                App.state.webView.loadUrl("javascript:clear()");

                App.state.menu.findItem(R.id.action_experiments).setVisible(true);
                App.state.menu.findItem(R.id.action_persist_recording).setVisible(false);
                App.state.menu.findItem(R.id.action_clear_recording).setVisible(false);
                break;
            case R.id.action_experiment_interval:
                final MainWebViewState state = App.state.webViewState;
                state.nextCurrentInterval();
                App.state.menu.findItem(R.id.action_experiment_interval).setTitle(state.getFormattedCurrentInterval());
                App.state.storage.sleepTime = state.getCurrentInterval();
                App.state.setWebViewState(state);
                restart();
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
                App.state.storage.persist(new Experiment(input.getText().toString().trim()));
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

                App.state.webView.loadUrl(String.format("javascript:loadExperiment('%s', %s)", experiment.name, stats));

                App.state.menu.findItem(R.id.action_to_main).setVisible(true);
                App.state.menu.findItem(R.id.action_repeat).setVisible(true);
                App.state.menu.findItem(R.id.action_to_stats).setVisible(false);
                App.state.menu.findItem(R.id.action_start_recording).setVisible(false);
            }
        });
        return builder;
    }

    private AlertDialog.Builder createBluetoothDevicesDialog() {
        final ArrayAdapter<Object> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        adapter.addAll(BluetoothSensorsManager.getPairedDevices());

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.bt_devices));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final BluetoothSensorsManager.BluetoothDeviceItem device = (BluetoothSensorsManager.BluetoothDeviceItem) adapter.getItem(which);
                bluetoothSensors.start(device.address);
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
                resume();

                App.state.webView.loadUrl(String.format("javascript:init(%s)", new Gson().toJson(App.state.webViewState)));

                final String page = App.state.webViewState.isMainPage ? "javascript:showMainPage()" : "javascript:showStatsPage()";
                App.state.webView.loadUrl(page);
            }
        });
        webView.loadUrl("file:///android_asset/web/index.html");

        return webView;
    }

    private void initMenu(final Menu menu) {
        final MainWebViewState state = App.state.webViewState;
        menu.findItem(R.id.action_experiment_interval).setVisible(true);
        menu.findItem(R.id.action_experiment_interval).setTitle(state.getFormattedCurrentInterval());

        menu.findItem(R.id.action_experiments).setVisible(true);

        menu.findItem(R.id.action_repeat).setVisible(false);

        menu.findItem(R.id.action_clear_recording).setVisible(false);
        menu.findItem(R.id.action_persist_recording).setVisible(false);

        menu.findItem(R.id.action_start_recording).setVisible(true);
        menu.findItem(R.id.action_stop_recording).setVisible(false);
    }

    private void resume() {
        bluetoothSensors.deinit();
        builtinSensors.start();
        appSensors.start();
    }

    private void exit() {
        App.state.storage.unexpectedPersist();
        App.state.storage.clear();

        appSensors.kill();
        builtinSensors.kill();
        bluetoothSensors.kill();
    }

    private void restart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appSensors.kill();
                appSensors = new AppSensorsWorker(builtinSensors, bluetoothSensors);
                appSensors.start();
            }
        }).start();
    }
}
