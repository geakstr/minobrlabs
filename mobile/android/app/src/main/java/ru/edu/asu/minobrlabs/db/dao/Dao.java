package ru.edu.asu.minobrlabs.db.dao;

import java.util.Collection;
import java.util.Date;

import nl.qbusict.cupboard.QueryResultIterable;
import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.Experiment;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Dao {
    public static QueryResultIterable findAll(final Class clazz) {
        return cupboard().withDatabase(App.db().conn())
                .query(clazz)
                .query();
    }

    public static QueryResultIterable findByDateRange(final Class clazz, final Long from, final Long to) {
        return cupboard().withDatabase(App.db().conn())
                .query(clazz)
                .withSelection("date >= ? and date <= ?", from.toString(), to.toString())
                .query();
    }

    public static QueryResultIterable findByDateRange(final Class clazz, final Date from, final Date to) {
        return findByDateRange(clazz, from.getTime(), to.getTime());
    }

    public static long put(Object o) {
        return cupboard().withDatabase(App.db().conn()).put(o);
    }

    public static void put(Object... o) {
        cupboard().withDatabase(App.db().conn()).put(o);
    }

    public static void put(Collection<?> o) {
        cupboard().withDatabase(App.db().conn()).put(o);
    }

    public static void delete(final Class clazz, final long id) {
        cupboard().withDatabase(App.db().conn()).delete(clazz, id);
    }

    public static void deleteByName(final Class clazz, final Experiment experiment) {
        cupboard().withDatabase(App.db().conn()).delete(clazz, "experiment = ?", experiment._id.toString());
    }

    public static void deleteAll(final Class clazz) {
        cupboard().withDatabase(App.db().conn()).delete(clazz, null);
    }
}
