package com.androidfu.foundation.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Bill on 8/1/14.
 */
@Parcel
public class ApplicationSettings {

    @SerializedName("version")
    public String someField;

    public ApplicationSettings() {
        // Parceler needs a public constructor
    }

    public ApplicationSettings(String string) {
        this.someField = string;
    }
}
