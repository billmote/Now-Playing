package com.androidfu.foundation.api;

import android.widget.ImageView;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/1/14.
 */
public class GetNetworkImageRequest {

    ImageView imageView;
    String url;

    private GetNetworkImageRequest() {
        // We really must have an ImageView and a URL so don't let this get instantiated without them.
    }

    @DebugLog
    public GetNetworkImageRequest(ImageView imageView, String url) {
        this.imageView = imageView;
        this.url = url;
    }
}
