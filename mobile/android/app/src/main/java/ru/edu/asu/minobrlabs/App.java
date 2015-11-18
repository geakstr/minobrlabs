package ru.edu.asu.minobrlabs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import ru.edu.asu.minobrlabs.db.Database;
import ru.edu.asu.minobrlabs.webview.MainWebViewState;

public class App extends Application {
    private static App singleton;

    private static Database db;

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;

        /* Добавление записи в БД
        Accel accel = new Accel("test");
        Experiment experiment = new Experiment("testname");
        db().experimentDao().put(experiment);
        accel.experiment = experiment;
        db().accelDao().put(accel);
        */
    }

    public static Database db() {
        if (null == db) {
            db = new Database();
        }
        return db;
    }

    public static App getInstance() {
        return singleton;
    }

    public static class Preferences {
        private static final String PREF_NAME = " ru.edu.asu.minobrlabs.MINOBRLABS_PREFERENCES";
        private static final int MODE = Context.MODE_PRIVATE;

        public static void writeBoolean(String key, boolean value) {
            getEditor().putBoolean(key, value).commit();
        }

        public static boolean readBoolean(String key, boolean defValue) {
            return getPreferences().getBoolean(key, defValue);
        }

        public static void writeInteger(String key, int value) {
            getEditor().putInt(key, value).commit();

        }

        public static int readInteger(String key, int defValue) {
            return getPreferences().getInt(key, defValue);
        }

        public static void writeString(String key, String value) {
            getEditor().putString(key, value).commit();
        }

        public static String readString(String key, String defValue) {
            return getPreferences().getString(key, defValue);
        }

        public static void writeLong(String key, long value) {
            getEditor().putLong(key, value).commit();
        }

        public static long readLong(String key, long defValue) {
            return getPreferences().getLong(key, defValue);
        }

        public static SharedPreferences getPreferences() {
            return App.getInstance().getApplicationContext().getSharedPreferences(PREF_NAME, MODE);
        }

        public static SharedPreferences.Editor getEditor() {
            return getPreferences().edit();
        }

        public static MainWebViewState readMainWebViewStateAsObject() {
            return new Gson().fromJson(readMainWebViewStateAsJson(), MainWebViewState.class);
        }

        public static String readMainWebViewStateAsJson() {
            String json = App.Preferences.readString("main_web_view_state", null);
            if (null == json) {
                final MainWebViewState state = new MainWebViewState();
                App.Preferences.writeMainWebViewState(state);
                json = new Gson().toJson(state);
            }
            return json;
        }

        public static void writeMainWebViewState(final MainWebViewState mainWebViewState) {
            App.Preferences.writeString("main_web_view_state", new Gson().toJson(mainWebViewState));
        }
    }
}
