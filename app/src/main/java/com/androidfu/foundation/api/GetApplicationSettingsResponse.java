package com.androidfu.foundation.api;

import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.util.SharedPreferencesHelper;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bill on 8/4/14.
 */
public class GetApplicationSettingsResponse {

    @SerializedName("results")
    public final ApplicationSettings applicationSettings;

    public GetApplicationSettingsResponse(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }
}

/*
Here's what we're dropping into GetApplicationSettingsResponse (as of 201408041358):

{
    "results": {
        "is_app_disabled": false,
        "update_nag_enabled": true,
        "production_version_number": 1,
        "low_water_mark_version_number": 1,
        "killswitch_message": "Testing my p33ps.  Go outside and play.",
        "mandatory_update_message": "A mandatory update is required in order for you to continue using this application.  Please forgive our mess.",
        "message_of_the_day": "Hope you're having a great day!",
        "update_nag_message": "A list of new features you'd get to use if you updated :)",
        "updatedAt": "2014-07-07T20:17:23.942Z"
    }
}

 */
