package com.androidfu.foundation.events;

import org.parceler.Parcel;

/**
 * Created by Bill on 8/4/14.
 */
@Parcel
public class NetworkAvailableEvent {

    public boolean isNetworkAvailable;
    public boolean isWifiAvailable;
    public boolean isMobileAvailable;

    private NetworkAvailableEvent() {
        // Don't let this get instantiated without our booleans
    }

    public NetworkAvailableEvent(boolean isWifiAvailable, boolean isMobileAvailable) {
        isNetworkAvailable = (isWifiAvailable || isMobileAvailable);
        this.isWifiAvailable = isWifiAvailable;
        this.isMobileAvailable = isMobileAvailable;

    }
}
