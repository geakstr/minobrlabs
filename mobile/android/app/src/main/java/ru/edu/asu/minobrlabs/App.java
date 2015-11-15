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

        /* Добавление записи в БД
        Accel accel = new Accel("test");
        Experiment experiment = new Experiment("testname");
        db().experimentDao().put(experiment);
        accel.experiment = experiment;
        db().accelDao().put(accel);
        */
    }

    public static Database db() {
        return db;
    }

    public static App getInstance() {
        return singleton;
    }
}
