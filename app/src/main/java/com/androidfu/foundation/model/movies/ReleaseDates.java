
package com.androidfu.foundation.model.movies;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
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
