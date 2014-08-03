package com.androidfu.foundation.handlers;

import android.widget.ImageView;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/1/14.
 */
public class GetNetworkImageRequest {

    ImageView imageView;
    String url;

    @DebugLog
    public GetNetworkImageRequest() {

    }

    @DebugLog
    public GetNetworkImageRequest(ImageView imageView, String url) {
        this.imageView = imageView;
        this.url = url;
    }
}
