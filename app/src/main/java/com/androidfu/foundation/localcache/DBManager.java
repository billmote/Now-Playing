package com.androidfu.foundation.localcache;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import hugo.weaving.DebugLog;

public class DBManager{

    private static DBHelper dbHelper = null;

    @DebugLog
    public static DBHelper getHelper(Context context){
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        }
        return dbHelper;
    }

    @DebugLog
    public static void releaseHelper(){
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }
}
