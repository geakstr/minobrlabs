package ru.edu.asu.minobrlabs.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.edu.asu.minobrlabs.Application;
import ru.edu.asu.minobrlabs.db.entities.Stat;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class CupboardSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "minobrlabs.db";
    private static final int DATABASE_VERSION = 1;

    static {
        cupboard().register(Stat.class);
    }

    public CupboardSQLiteOpenHelper() {
        super(Application.getInstance().getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }
}
