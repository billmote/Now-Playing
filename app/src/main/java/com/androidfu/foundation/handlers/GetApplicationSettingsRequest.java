package com.androidfu.foundation.handlers;

import android.widget.ImageView;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/1/14.
 */
public class GetApplicationSettingsRequest {

    ImageView imageView;
    String url;

    @DebugLog
    public GetApplicationSettingsRequest() {

    }

    @DebugLog
    public GetApplicationSettingsRequest(ImageView imageView, String url) {
        this.imageView = imageView;
        this.url = url;
    }
}
