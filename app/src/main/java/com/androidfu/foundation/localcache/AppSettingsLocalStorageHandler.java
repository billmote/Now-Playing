package com.androidfu.foundation.localcache;

import android.content.Context;

import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.model.Version;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
public class AppSettingsLocalStorageHandler extends LocalStorageHandler<ApplicationSettings> {

    private final Context context;

    @DebugLog
    public AppSettingsLocalStorageHandler(Context ctx){
        this.context = ctx;
    }

    @DebugLog
    @Override
    protected Dao getDao() {
        return DBManager.getHelper(this.context).getApplicationSettingsDao();
    }

    @DebugLog
    public ApplicationSettings getCurrentApplicationSettings() throws SQLException {
        List<ApplicationSettings> appset = this.getDao().queryForAll();
        if (appset!=null && appset.size()>0){
            return appset.get(0);
        }else{
            return null;
        }
    }

    @DebugLog
    public void saveCurrentApplicationSettings(ApplicationSettings appset) throws SQLException {
        TableUtils.clearTable(this.getDao().getConnectionSource(), ApplicationSettings.class);
        this.getDao().create(appset);
        Dao versionDao = DBManager.getHelper(this.context).getVersionDao();
        TableUtils.clearTable(versionDao.getConnectionSource(), Version.class);
        for (Version version : appset.getVersions()) {
            // ApplicationSettings dao does not save Version. Only Version dao does that
            // ApplicationSettings.getVersionCollections() will make a call to the "version"
            // db table referencing all Version rows with ApplicationSettings.id
            version.setApplicationSettings(appset);
            versionDao.create(version);
        }
    }
}