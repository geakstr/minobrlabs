package ru.edu.asu.minobrlabs;

import android.app.Application;

import ru.edu.asu.minobrlabs.db.Database;

public class App extends Application {
    private static App singleton;

    private static Database db;

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;
        db = new Database();

        // TODO: Remove this line
        db().statDao().delete();
    }

    public static Database db() {
        return db;
    }

    public static App getInstance() {
        return singleton;
    }
}
