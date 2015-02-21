package com.androidfu.foundation.events.application;

import com.androidfu.foundation.events.BaseEvent;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class GetApplicationSettingsEvent extends BaseEvent {
    public GetApplicationSettingsEvent(int callNumber) {
        super(callNumber);
    }
}