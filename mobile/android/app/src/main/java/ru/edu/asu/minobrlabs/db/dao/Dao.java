package ru.edu.asu.minobrlabs.db.dao;

import java.util.Date;

import nl.qbusict.cupboard.QueryResultIterable;
import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.TestName;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Dao<T> {
    private final Class clazz;

    public Dao(Class clazz) {
        this.clazz = clazz;
    }

    public long put(final T o) {
        return cupboard().withDatabase(App.db().conn()).put(o);
    }

    public void delete(final T o) {
        cupboard().withDatabase(App.db().conn()).delete(o);
    }

    public QueryResultIterable<T> findByDateRange(final Long from, final Long to) {
        return cupboard().withDatabase(App.db().conn())
                .query(clazz)
                .withSelection("date >= ? and date <= ?", from.toString(), to.toString())
                .query();
    }

    public QueryResultIterable<T> findByDateRange(final Date from, final Date to) {
        return findByDateRange(from.getTime(), to.getTime());
    }

    public void delete(final long id) {
        cupboard().withDatabase(App.db().conn()).delete(clazz, id);
    }

    public void deleteByName(final TestName name) {
        cupboard().withDatabase(App.db().conn()).delete(clazz, "name = ?", name._id.toString());
    }

    public void deleteAll() {
        cupboard().withDatabase(App.db().conn()).delete(clazz, null);
    }
}
