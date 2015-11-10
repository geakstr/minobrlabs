package ru.edu.asu.minobrlabs.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.edu.asu.minobrlabs.Application;
import ru.edu.asu.minobrlabs.db.dao.StatDao;
import ru.edu.asu.minobrlabs.db.entities.Stat;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "minobrlabs.db";
    private static final int DATABASE_VERSION = 1;

    private final SQLiteDatabase conn;
    private final StatDao statDao;

    static {
        cupboard().register(Stat.class);
    }

    public Database() {
        super(Application.getInstance().getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);

        this.conn = getWritableDatabase();
        this.statDao = new StatDao();
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

    public SQLiteDatabase conn() {
        return conn;
    }

    public StatDao statDao() {
        return statDao;
    }
}
