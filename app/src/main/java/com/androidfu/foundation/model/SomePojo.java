package com.androidfu.foundation.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Bill on 8/1/14.
 */
@Parcel
public class SomePojo {

    @SerializedName("title")
    public String someField;

    public SomePojo() {
        // Parceler needs a public constructor
    }

    public SomePojo (String string) {
        this.someField = string;
    }
}
