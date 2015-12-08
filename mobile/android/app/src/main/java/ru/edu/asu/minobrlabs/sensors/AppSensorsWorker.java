package ru.edu.asu.minobrlabs.sensors;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.sensors.bluetooth.BluetoothSensorsManager;
import ru.edu.asu.minobrlabs.sensors.bluetooth.BluetoothSensorsWorker;
import ru.edu.asu.minobrlabs.sensors.builtin.BuiltinSensorsManager;
import ru.edu.asu.minobrlabs.sensors.builtin.BuiltinSensorsWorker;
import ru.edu.asu.minobrlabs.sensors.common.AbstractWorker;

public class AppSensorsWorker extends AbstractWorker {
    public static final String TAG = AppSensorsWorker.class.getSimpleName();

    private boolean init;

    private final BuiltinSensorsWorker builtinWorker;
    private final BluetoothSensorsWorker bluetoothWorker;

    private final AppSensorsHandler handler;

    public AppSensorsWorker(final BuiltinSensorsManager builtinSensorsManager, final BluetoothSensorsManager bluetoothSensorsManager) {
        super();

        this.init = false;

        this.builtinWorker = new BuiltinSensorsWorker(builtinSensorsManager);
        this.bluetoothWorker = new BluetoothSensorsWorker(bluetoothSensorsManager);

        this.handler = new AppSensorsHandler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {
            while (running) {
                handler.obtainMessage().sendToTarget();
                sleep(App.state.storage.sleepTime);
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "App sensors manager worker problems", e);
        }
    }

    @Override
    public void start() {
        if (!init) {
            init = true;

            builtinWorker.start();
            bluetoothWorker.start();

            Log.d(TAG, "Start");

            super.start();
        }
    }

    @Override
    public void kill() {
        if (init) {
            init = false;

            builtinWorker.kill();
            bluetoothWorker.kill();

            Log.d(TAG, "Kill");

            super.kill();
        }
    }

    private static class AppSensorsHandler extends Handler {
        public AppSensorsHandler(final Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (App.state.storage.wantReInit) {
                App.state.storage.wantReInit = false;

                final String state = App.Preferences.readMainWebViewStateAsJson();
                App.state.webView.loadUrl(String.format("javascript:init(%s)", state));
                return;
            }

            final String updates = App.state.storage.updatesToString();
            if (null != updates) {
                App.state.webView.loadUrl("javascript:update(" + updates + ")");
            }
        }
    }
}
