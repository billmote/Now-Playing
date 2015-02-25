package com.androidfu.nowplaying.app.localcache;

import android.database.Cursor;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public abstract class LocalStorageHandler<T> {

    protected abstract Dao<T, Integer> getDao();

    public Cursor getCursorForAll() throws SQLException {
        QueryBuilder<T, Integer> qb = this.getDao().queryBuilder();
        qb.where().eq("deleted", false);
        return this.getCursor(qb);
    }

    public Cursor getCursor(QueryBuilder<T, Integer> qb) throws SQLException {
        CloseableIterator<T> iterator = this.getDao().iterator(qb.prepare());
        try {
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            return results.getRawCursor();
        } finally {
            iterator.closeQuietly();
        }
    }

    public List<T> getAll() throws SQLException {
        return this.getDao().queryForEq("deleted", false);
    }

    /* Save */
    public T save(T object) throws SQLException {
        this.getDao().createOrUpdate(object);
        return object;
    }

    public List<T> save(List<T> objects) throws SQLException {
        ArrayList<T> result = new ArrayList<T>();
        for (T o : objects) {
            result.add(this.save(o));
        }
        return result;
    }

    /* Delete */
    public void delete(T object) throws SQLException {
        this.getDao().delete(object);
    }

    public void delete(List<T> objects) throws SQLException {
        for (T o : objects) {
            this.delete(o);
        }
    }
}
