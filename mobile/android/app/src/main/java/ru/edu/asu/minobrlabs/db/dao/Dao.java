package ru.edu.asu.minobrlabs.db.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.Database;
import ru.edu.asu.minobrlabs.db.entities.Experiment;
import ru.edu.asu.minobrlabs.db.entities.GenericParam;
import ru.edu.asu.minobrlabs.sensors.SensorTypes;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Dao {
    private static Database db;

    public static Database db() {
        if (null == db) {
            db = new Database();
        }
        return db;
    }

    public static List findAll(final Class clazz) {
        return cupboard().withDatabase(db().conn())
                .query(clazz)
                .query().list();
    }

    public static Map<String, List<Object>> findByExperiment(final Experiment experiment) {
        final String id = experiment._id.toString();

        final Map<String, List<Object>> map = new HashMap<>();
        for (final SensorTypes sensorType : SensorTypes.values()) {
            final String name = sensorType.getName();
            final Class clazz = sensorType.getClazz();

            map.put(name, cupboard().withDatabase(db().conn())
                    .query(clazz)
                    .withSelection("experimentId = ?", id)
                    .query().list());
        }
        return map;
    }

    public static List findByDateRange(final Class clazz, final Long from, final Long to) {
        return cupboard().withDatabase(db().conn())
                .query(clazz)
                .withSelection("date >= ? and date <= ?", from.toString(), to.toString())
                .query().list();
    }

    public static List findByDateRange(final Class clazz, final Date from, final Date to) {
        return findByDateRange(clazz, from.getTime(), to.getTime());
    }

    public static long put(Object o) {
        return cupboard().withDatabase(db().conn()).put(o);
    }

    public static void put(Object... o) {
        cupboard().withDatabase(db().conn()).put(o);
    }

    public static void put(Collection<?> o) {
        cupboard().withDatabase(db().conn()).put(o);
    }

    public static void delete(final Class clazz, final long id) {
        cupboard().withDatabase(db().conn()).delete(clazz, id);
    }

    public static void deleteByExperimentId(final Class clazz, final Experiment experiment) {
        cupboard().withDatabase(db().conn()).delete(clazz, "experimentId = ?", experiment._id.toString());
    }

    public static void deleteAll(final Class clazz) {
        cupboard().withDatabase(db().conn()).delete(clazz, null);
    }
}
