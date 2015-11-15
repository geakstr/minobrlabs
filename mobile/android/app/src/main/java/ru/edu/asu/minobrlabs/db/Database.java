package ru.edu.asu.minobrlabs.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.dao.Dao;
import ru.edu.asu.minobrlabs.db.entities.params.Accel;
import ru.edu.asu.minobrlabs.db.entities.params.AirPressure;
import ru.edu.asu.minobrlabs.db.entities.params.AirTemperature;
import ru.edu.asu.minobrlabs.db.entities.params.Amperage;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.db.entities.params.Gyro;
import ru.edu.asu.minobrlabs.db.entities.params.Humidity;
import ru.edu.asu.minobrlabs.db.entities.params.Light;
import ru.edu.asu.minobrlabs.db.entities.params.SoluteTemperature;
import ru.edu.asu.minobrlabs.db.entities.params.Microphone;
import ru.edu.asu.minobrlabs.db.entities.params.PH;
import ru.edu.asu.minobrlabs.db.entities.params.Voltage;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "minobrlabs.db";
    private static final int DATABASE_VERSION = 1;

    private final SQLiteDatabase conn;

    private final Dao<Experiment> experimentDao;

    private final Dao<Accel> accelDao;
    private final Dao<Light> lightDao;
    private final Dao<Gyro> gyroDao;
    private final Dao<AirTemperature> airTemperatureDao;
    private final Dao<SoluteTemperature> soluteTemperatureDao;
    private final Dao<Voltage> voltageDao;
    private final Dao<Amperage> amperageDao;
    private final Dao<Microphone> microphoneDao;
    private final Dao<AirPressure> airPressureDao;
    private final Dao<PH> pHDao;
    private final Dao<Humidity> humidityDao;

    static {
        cupboard().register(Experiment.class);
        cupboard().register(Gyro.class);
        cupboard().register(Humidity.class);
        cupboard().register(AirTemperature.class);
        cupboard().register(Accel.class);
        cupboard().register(Light.class);
        cupboard().register(Microphone.class);
        cupboard().register(SoluteTemperature.class);
        cupboard().register(PH.class);
        cupboard().register(AirPressure.class);
        cupboard().register(Voltage.class);
        cupboard().register(Amperage.class);
    }

    public Database() {
        super(App.getInstance().getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);

        this.conn = getWritableDatabase();
        this.accelDao = new Dao<>(Accel.class);
        this.airPressureDao = new Dao<>(AirPressure.class);
        this.airTemperatureDao = new Dao<>(AirTemperature.class);
        this.amperageDao = new Dao<>(Amperage.class);
        this.gyroDao = new Dao<>(Gyro.class);
        this.humidityDao = new Dao<>(Humidity.class);
        this.lightDao = new Dao<>(Light.class);
        this.soluteTemperatureDao = new Dao<>(SoluteTemperature.class);
        this.microphoneDao = new Dao<>(Microphone.class);
        this.pHDao = new Dao<>(PH.class);
        this.experimentDao = new Dao<>(Experiment.class);
        this.voltageDao = new Dao<>(Voltage.class);
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

    public Dao<Accel> accelDao() {
        return accelDao;
    }

    public Dao<AirPressure> airPressureDao() {
        return airPressureDao;
    }

    public Dao<AirTemperature> airTemperatureDao() {
        return airTemperatureDao;
    }

    public Dao<Amperage> amperageDao() {
        return amperageDao;
    }

    public Dao<Gyro> gyroDao() {
        return gyroDao;
    }

    public Dao<Humidity> humidityDao() {
        return humidityDao;
    }

    public Dao<Light> lightDao() {
        return lightDao;
    }

    public Dao<SoluteTemperature> liquidTemperatureDao() {
        return soluteTemperatureDao;
    }

    public Dao<Microphone> microphoneDao() {
        return microphoneDao;
    }

    public Dao<PH> pHValueDao() {
        return pHDao;
    }

    public Dao<Experiment> experimentDao() {
        return experimentDao;
    }

    public Dao<Voltage> voltageDao() {
        return voltageDao;
    }
}
