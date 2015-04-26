package com.androidfu.nowplaying.app.api;

import android.content.Context;

import com.androidfu.nowplaying.app.util.Log;
import com.androidfu.nowplaying.app.util.Preconditions;
import com.jakewharton.byteunits.DecimalByteUnit;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import hugo.weaving.DebugLog;

/**
 *
 */
@DebugLog
public final class CachingOkHttpClient {
    private static final String TAG = "CachingOkHttpClient";
    private static final int DEFAULT_CONNECT_TIMEOUT = 10;
    private static final int DEFAULT_WRITE_TIMEOUT = 10;
    private static final int DEFAULT_READ_TIMEOUT = 10;
    private static final long DEFAULT_DISK_CACHE_SIZE = DecimalByteUnit.MEGABYTES.toBytes(10);
    private static volatile CachingOkHttpClient instance = null;
    private final OkHttpClient client;
    private Cache cache;

    private CachingOkHttpClient(final Builder builder) {
        this.client = new OkHttpClient();
        this.client.setConnectTimeout(builder.connectTimeout, TimeUnit.SECONDS);
        this.client.setWriteTimeout(builder.writeTimeout, TimeUnit.SECONDS);
        this.client.setReadTimeout(builder.readTimeout, TimeUnit.SECONDS);
        try {
            if (cache == null) {
                Log.i(TAG, String.format("Creating response cache of %d bytes ...", DEFAULT_DISK_CACHE_SIZE));
                cache = new Cache(builder.directory, builder.diskCacheSize);
                Log.i(TAG, "success.");
            }
        } catch (IOException e) {
            Log.e(TAG, String.format("Error creating response cache: %s", e.getMessage()), e);
        }
        if (cache != null) {
            client.setCache(cache);
        }
    }

    /*
        A static alternative to the Builder().
     */
    public static CachingOkHttpClient with(Context context) {
        if (instance == null) {
            synchronized (CachingOkHttpClient.class) {
                if (instance == null) {
                    instance = new Builder(context).build();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getClient() {
        return client.clone(); //Shallow copy of OkHttpClient. All downstream clients share the same cache and each client can set their own interceptors etc.
    }

    public static class Builder {
        private final Context context;
        private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        private int writeTimeout = DEFAULT_WRITE_TIMEOUT;
        private int readTimeout = DEFAULT_READ_TIMEOUT;
        private long diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
        private File directory;

        public Builder(Context context) {
            Preconditions.checkNotNull(context, "context != null");
            this.context = context;
        }

        public Builder connectTimeout(int connectTimeout) {
            Preconditions.checkArgument(connectTimeout > 0, "connectTimeout must be > 0");
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder writeTimeout(int writeTimeout) {
            Preconditions.checkArgument(writeTimeout > 0, "writeTimeout must be > 0");
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder readTimeout(int readTimeout) {
            Preconditions.checkArgument(readTimeout > 0, "readTimeout must be > 0");
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder diskCacheSize(long diskCacheSize) {
            Preconditions.checkArgument(diskCacheSize > 0, "diskSize must be > 0");
            this.diskCacheSize = diskCacheSize;
            return this;
        }

        public Builder file(File directory) {
            Preconditions.checkNotNull(directory, "directory != null");
            this.directory = directory;
            return this;
        }

        public CachingOkHttpClient build() {
            ensureSaneDefaults();
            return new CachingOkHttpClient(this);
        }

        private void ensureSaneDefaults() {
            if (directory == null) {
                directory = new File(context.getCacheDir(), "http");
            }
        }
    }
}