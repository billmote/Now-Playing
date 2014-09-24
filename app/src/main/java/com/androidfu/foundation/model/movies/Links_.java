
package com.androidfu.foundation.model.movies;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class Links_ {

    @Expose
    private String self;
    @Expose
    private String alternate;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getAlternate() {
        return alternate;
    }

    public void setAlternate(String alternate) {
        this.alternate = alternate;
    }

}
