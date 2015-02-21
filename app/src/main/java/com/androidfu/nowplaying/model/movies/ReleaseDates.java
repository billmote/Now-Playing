
package com.androidfu.nowplaying.model.movies;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import hugo.weaving.DebugLog;

@Parcel
@DebugLog
public class ReleaseDates {

    @Expose
    private String theater;

    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

}
