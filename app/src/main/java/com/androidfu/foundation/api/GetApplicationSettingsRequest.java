package com.androidfu.foundation.api;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/1/14.
 */
public class GetApplicationSettingsRequest {

    String url;

    @DebugLog
    private GetApplicationSettingsRequest() {
        // We must have our fake webservice URL
    }

    @DebugLog
    public GetApplicationSettingsRequest(String url) {
        this.url = url;
    }

}
