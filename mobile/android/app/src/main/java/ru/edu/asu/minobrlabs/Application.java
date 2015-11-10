package ru.edu.asu.minobrlabs;

import android.database.sqlite.SQLiteDatabase;

import ru.edu.asu.minobrlabs.db.CupboardSQLiteOpenHelper;
import ru.edu.asu.minobrlabs.db.dao.StatDao;

public class Application extends android.app.Application {
    private static Application singleton;

    private static CupboardSQLiteOpenHelper sqlite;
    private static SQLiteDatabase db;
    private static StatDao statDao;

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;

        // TODO: Remove this line
        statDao().delete();
    }

    public static CupboardSQLiteOpenHelper sqlite() {
        if (null == sqlite) {
            sqlite = new CupboardSQLiteOpenHelper();
        }
        return sqlite;
    }

    public static SQLiteDatabase db() {
        if (null == db) {
            db = sqlite().getWritableDatabase();
        }
        return db;
    }

    public static StatDao statDao() {
        if (null == statDao) {
            statDao = new StatDao();
        }
        return statDao;
    }

    public static Application getInstance() {
        return singleton;
    }
}
