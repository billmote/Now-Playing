package com.androidfu.foundation.localcache;

import android.content.Context;

import com.androidfu.foundation.model.ApplicationSettings;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by billmote on 9/7/14.
 */
public class AppSettingsLocalStorageHandler extends LocalStorageHandler<ApplicationSettings> {

    private final Context context;

    public AppSettingsLocalStorageHandler(Context ctx){
        this.context = ctx;
    }

    @Override
    protected Dao getDao() {
        return DBManager.getHelper(this.context).getApplicationSettingsDao();
    }

    public ApplicationSettings getCurrentApplicationSettings() throws SQLException {
        List<ApplicationSettings> appset = this.getDao().queryForAll();
        if (appset!=null && appset.size()>0){
            return appset.get(0);
        }else{
            return null;
        }
    }

    public void saveCurrentApplicationSettings(ApplicationSettings appset) throws SQLException {
        TableUtils.clearTable(this.getDao().getConnectionSource(), ApplicationSettings.class);
        this.getDao().create(appset);
    }

}