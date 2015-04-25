package com.androidfu.nowplaying.app.api;

import android.os.Environment;

import com.androidfu.nowplaying.app.util.Log;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 4/24/15.
 */
@DebugLog
public class CachingOkHttpClient {

    private static final String TAG = "CachingOkHttpClient";
    public static final int CONNECT_TIMEOUT = 10;
    public static final int WRITE_TIMEOUT = 10;
    public static final int READ_TIMEOUT = 30;
    public static final int CACHE_SIZE_IN_BYTES = 10485760;

    private static CachingOkHttpClient instance;
    private OkHttpClient client;
    private Cache cache;

    private CachingOkHttpClient() {
        try {
            Log.i(TAG, String.format("Creating response cache of %d bytes ...", CACHE_SIZE_IN_BYTES));
            if (cache == null) {
                cache = new Cache(Environment.getExternalStorageDirectory(), CACHE_SIZE_IN_BYTES);
                Log.i(TAG, "success.");
            }
        } catch (IOException e) {
            Log.e(TAG, String.format("Error creating response cache: %s", e.getMessage()), e);
        }
        client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        client.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        if (cache != null) {
            client.setCache(cache);
        }
    }

    public static OkHttpClient getClient() {
        return getInstance().client;
    }

    public static CachingOkHttpClient getInstance() {
        if (instance == null) {
            instance = new CachingOkHttpClient();
        }
        return instance;
    }
}
