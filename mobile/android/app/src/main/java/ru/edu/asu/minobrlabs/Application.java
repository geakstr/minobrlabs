package ru.edu.asu.minobrlabs;

import ru.edu.asu.minobrlabs.db.Database;

public class Application extends android.app.Application {
    private static Application singleton;

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

    public static Application getInstance() {
        return singleton;
    }
}
