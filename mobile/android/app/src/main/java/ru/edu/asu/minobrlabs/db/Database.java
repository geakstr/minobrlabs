package ru.edu.asu.minobrlabs.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.dao.Dao;
import ru.edu.asu.minobrlabs.db.entities.Accel;
import ru.edu.asu.minobrlabs.db.entities.AirPressure;
import ru.edu.asu.minobrlabs.db.entities.AirTemperature;
import ru.edu.asu.minobrlabs.db.entities.Amperage;
import ru.edu.asu.minobrlabs.db.entities.Gyro;
import ru.edu.asu.minobrlabs.db.entities.Humidity;
import ru.edu.asu.minobrlabs.db.entities.Light;
import ru.edu.asu.minobrlabs.db.entities.SoluteTemperature;
import ru.edu.asu.minobrlabs.db.entities.Microphone;
import ru.edu.asu.minobrlabs.db.entities.PH;
import ru.edu.asu.minobrlabs.db.entities.TestName;
import ru.edu.asu.minobrlabs.db.entities.Voltage;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "minobrlabs.db";
    private static final int DATABASE_VERSION = 1;

    private final SQLiteDatabase conn;
    private final Dao<Accel> accelDao;
    private final Dao<AirPressure> airPressureDao;
    private final Dao<AirTemperature> airTemperatureDao;
    private final Dao<Amperage> amperageDao;
    private final Dao<Gyro> gyroDao;
    private final Dao<Humidity> humidityDao;
    private final Dao<Light> lightDao;
    private final Dao<SoluteTemperature> liquidTemperatureDao;
    private final Dao<Microphone> microphoneDao;
    private final Dao<PH> pHValueDao;
    private final Dao<TestName> testNameDao;
    private final Dao<Voltage> voltageDao;

    static {
        cupboard().register(TestName.class);
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
        accelDao = new Dao<>(Accel.class);
        airPressureDao = new Dao<>(AirPressure.class);
        airTemperatureDao = new Dao<>(AirTemperature.class);
        amperageDao = new Dao<>(Amperage.class);
        gyroDao = new Dao<>(Gyro.class);
        humidityDao = new Dao<>(Humidity.class);
        lightDao = new Dao<>(Light.class);
        liquidTemperatureDao = new Dao<>(SoluteTemperature.class);
        microphoneDao = new Dao<>(Microphone.class);
        pHValueDao = new Dao<>(PH.class);
        testNameDao = new Dao<>(TestName.class);
        voltageDao = new Dao<>(Voltage.class);
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
        return liquidTemperatureDao;
    }

    public Dao<Microphone> microphoneDao() {
        return microphoneDao;
    }

    public Dao<PH> pHValueDao() {
        return pHValueDao;
    }

    public Dao<TestName> testNameDao() {
        return testNameDao;
    }

    public Dao<Voltage> voltageDao() {
        return voltageDao;
    }
}
