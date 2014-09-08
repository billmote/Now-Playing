package com.androidfu.foundation.localcache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper{
    private static final String DATABASE_NAME = "foundation.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<ApplicationSettings, Integer> applicationSettingsDao;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

        try {
            Log.i(DBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, ApplicationSettings.class);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DBHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, ApplicationSettings.class, true);
            this.onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<ApplicationSettings, Integer> getApplicationSettingsDao() {
        if (this.applicationSettingsDao == null) {
            try {
                this.applicationSettingsDao = this.getDao(ApplicationSettings.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.applicationSettingsDao;
    }

    @Override
    public void close() {
        super.close();
        this.applicationSettingsDao = null;
    }
}