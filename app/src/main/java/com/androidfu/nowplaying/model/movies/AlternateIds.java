
package com.androidfu.nowplaying.model.movies;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import hugo.weaving.DebugLog;

@Parcel
@DebugLog
public class AlternateIds {

    @Expose
    public String imdb;

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

}
