package com.androidfu.foundation.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Our ApplicationSettings class has it's parcelable descriptors created with Parceler via the
 * Parcel annotation.  Note: Parceler is exponentially faster when the members are public.  That
 * does not preclude us from having getters/setters though.
 * <p/>
 * Created by Bill on 8/1/14.
 */
@Parcel
public class ApplicationSettings {

    public static final String TAG = ApplicationSettings.class.getSimpleName();

    @SerializedName("is_app_disabled")
    public boolean isAppDisabled;

    @SerializedName("update_nag_enabled")
    public boolean isNagEnabled;

    @SerializedName("production_version_number")
    public int productionVersionNumber;

    @SerializedName("low_water_mark_version_number")
    public int lowWaterMarkVersionNumber;

    @SerializedName("killswitch_message")
    public String killswitchMessage;

    @SerializedName("mandatory_update_message")
    public String mandatoryUpdateMessage;

    @SerializedName("message_of_the_day")
    public String messageOfTheDay;

    @SerializedName("update_nag_message")
    public String updateNagMessage;

    public Long updatedAt;

}
