package ru.edu.asu.minobrlabs.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.params.Accel;
import ru.edu.asu.minobrlabs.db.entities.params.AtmoPressure;
import ru.edu.asu.minobrlabs.db.entities.params.AirTemperature;
import ru.edu.asu.minobrlabs.db.entities.params.Amperage;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.db.entities.params.Gyro;
import ru.edu.asu.minobrlabs.db.entities.params.Humidity;
import ru.edu.asu.minobrlabs.db.entities.params.Light;
import ru.edu.asu.minobrlabs.db.entities.params.SoluteTemperature;
import ru.edu.asu.minobrlabs.db.entities.params.Microphone;
import ru.edu.asu.minobrlabs.db.entities.params.Ph;
import ru.edu.asu.minobrlabs.db.entities.params.Voltage;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "minobrlabs.db";
    private static final int DATABASE_VERSION = 1;

    private final SQLiteDatabase conn;

    static {
        cupboard().register(Experiment.class);
        cupboard().register(Gyro.class);
        cupboard().register(Humidity.class);
        cupboard().register(AirTemperature.class);
        cupboard().register(Accel.class);
        cupboard().register(Light.class);
        cupboard().register(Microphone.class);
        cupboard().register(SoluteTemperature.class);
        cupboard().register(Ph.class);
        cupboard().register(AtmoPressure.class);
        cupboard().register(Voltage.class);
        cupboard().register(Amperage.class);
    }

    public Database() {
        super(App.getInstance().getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);

        this.conn = getWritableDatabase();
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
}
