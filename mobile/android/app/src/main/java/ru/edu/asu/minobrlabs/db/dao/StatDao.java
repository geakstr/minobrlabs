package ru.edu.asu.minobrlabs.db.dao;

import java.util.Date;

import nl.qbusict.cupboard.QueryResultIterable;
import ru.edu.asu.minobrlabs.App;
import ru.edu.asu.minobrlabs.db.entities.Stat;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class StatDao {
    public long put(final Stat stat) {
        return cupboard().withDatabase(App.db().conn()).put(stat);
    }

    public QueryResultIterable<Stat> findByType(final Integer type) {
        return cupboard().withDatabase(App.db().conn())
                .query(Stat.class)
                .withSelection("type = ?", type.toString())
                .query();
    }

    public QueryResultIterable<Stat> findByDateRange(final Long from, final Long to) {
        return cupboard().withDatabase(App.db().conn())
                .query(Stat.class)
                .withSelection("date >= ? and date <= ?", from.toString(), to.toString())
                .query();
    }

    public QueryResultIterable<Stat> findByDateRange(final Date from, final Date to) {
        return findByDateRange(from.getTime(), to.getTime());
    }

    public void delete(final long id) {
        cupboard().withDatabase(App.db().conn()).delete(Stat.class, id);
    }

    public void delete(final Stat stat) {
        cupboard().withDatabase(App.db().conn()).delete(stat);
    }

    public void delete(final String where, final String... params) {
        cupboard().withDatabase(App.db().conn()).delete(Stat.class, where, params);
    }

    public void delete(final String where) {
        cupboard().withDatabase(App.db().conn()).delete(Stat.class, where);
    }

    public void delete() {
        cupboard().withDatabase(App.db().conn()).delete(Stat.class, null);
    }
}
