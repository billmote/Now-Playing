package com.androidfu.foundation.localcache;

import android.database.Cursor;

import com.androidfu.foundation.model.BaseModel;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by billmote on 9/7/14.
 */
public abstract class LocalStorageHandler<T extends BaseModel> {

    protected abstract Dao<T,Integer> getDao();

    /* Get */
    public T get(String remoteId) throws SQLException {
        return this.getByRemoteId(remoteId);
    }

    public Cursor getCursorForAll() throws SQLException{
        QueryBuilder<T,Integer> qb = this.getDao().queryBuilder();
        qb.where().eq("deleted",false);
        return this.getCursor(qb);
    }

    public Cursor getCursor(QueryBuilder<T,Integer> qb) throws SQLException{
        CloseableIterator<T> iterator = this.getDao().iterator(qb.prepare());
        try {
            AndroidDatabaseResults results = (AndroidDatabaseResults)iterator.getRawResults();
            return results.getRawCursor();
        } finally {
            iterator.closeQuietly();
        }
    }

    public List<T> getAll() throws SQLException{
        return this.getDao().queryForEq("deleted",false);

    }

    public T getByLocalId(int localId) throws SQLException {
        if (localId > 0) {
            return this.getDao().queryForId(localId);
        }else{
            return null;
        }
    }

    public T getByRemoteId(String remoteId) throws SQLException{
        if (remoteId!=null && !remoteId.isEmpty()) {
            List<T> result = this.getDao().queryForEq("remoteid", remoteId);
            if (result != null && result.size() > 0) {
                return result.get(0);
            } else {
                return null;
            }
        }else{
            return null;
        }
    }

    /* Save */
    public T save(T object) throws SQLException {
        T dbObject = this.getByRemoteId(object.getRemoteid());
        if (dbObject!=null) {
            object.setLocalid(dbObject.getLocalid());
        }else if (object.getLocalid() > 0){
            dbObject = this.getByLocalId(object.getLocalid());
        }

        this.getDao().createOrUpdate(object);

        return object;
    }

    public List<T> save(List<T> objects) throws SQLException {
        ArrayList<T> result = new ArrayList<T>();

        for (T o : objects){
            result.add(this.save(o));
        }

        return result;
    }

    /* Delete One */
    public void delete(String remoteId) throws SQLException {
        this.deleteByRemoteId(remoteId);
    }

    public void delete(T object) throws SQLException {
        this.deleteByRemoteId(object.getRemoteid());
    }


    public void deleteByRemoteId(String remoteId) throws SQLException {
        T dbObject = this.getByRemoteId(remoteId);
        this.getDao().delete(dbObject);
    }

    public void deleteByLocalId(int localId) throws SQLException {
        this.getDao().deleteById(localId);
    }

    public void softDeleteByLocalId(int localId) throws SQLException {
        T dbObject = this.getDao().queryForId(localId);
        dbObject.setDeleted(true);
        this.getDao().update(dbObject);
    }

    /* Delete multiple */
    public void delete(String[] remoteIds) throws SQLException {
        this.deleteByRemoteIds(remoteIds);
    }

    public void delete(List<T> objects) throws SQLException {
        for (T o : objects){
            this.delete(o);
        }
    }

    public void deleteByRemoteIds(String[] remoteIds) throws SQLException {
        for (String i : remoteIds){
            this.deleteByRemoteId(i);
        }
    }

    public void deleteByLocalIds(int[] localIds) throws SQLException {
        for (int i : localIds){
            this.deleteByLocalId(i);
        }
    }

    public void clean(T object) throws SQLException {
        object.setDirty(false);
        this.save(object);
    }

    public void unclean(T object) throws SQLException {
        object.setDirty(true);
        this.save(object);
    }
}