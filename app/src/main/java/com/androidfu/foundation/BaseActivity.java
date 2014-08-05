package com.androidfu.foundation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidfu.foundation.api.GetApplicationSettingsRequest;
import com.androidfu.foundation.events.NetworkAvailableEvent;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.Log;
import com.androidfu.foundation.util.SharedPreferencesHelper;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import hugo.weaving.DebugLog;

public class BaseActivity extends Activity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    private static NetworkListener networkListener;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkListener = new NetworkListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.unregister(networkListener);
    }

    @DebugLog
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG && menu.findItem(R.id.force_crash) == null) {
            // Only show these options for developer builds
            menu.add(0, R.id.force_crash, 0, R.string.force_crash);
            menu.add(0, R.id.reset_prefs, 0, R.string.reset_application_preferences);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle action buttons
        switch (item.getItemId()) {

            case R.id.force_crash:
                Log.wtf(TAG, "Throwing a new RuntimeException.  Yes, on purpose.");
                Toast.makeText(this, "Throwing a new RuntimeException", Toast.LENGTH_SHORT).show();
                throw new RuntimeException("Forced Crash");

            case R.id.reset_prefs:
                SharedPreferencesHelper.clear();
                Log.i(TAG, "Shared Application Preferences Cleared.");
                Toast.makeText(this, "Shared Application Preferences Cleared", Toast.LENGTH_SHORT).show();
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Otto only looks for 1st class objects.  If you want to subscribe to something in a parent class
     * you need to do so like this.
     */
    public class NetworkListener {

        public final String TAG = NetworkListener.class.getSimpleName();

        boolean isWifiAvailable;
        boolean isMobileAvailable;

        @DebugLog
        NetworkListener() {
            EventBus.register(this);
        }

        @DebugLog
        @Produce
        public NetworkAvailableEvent lastNetworkState() {
            return new NetworkAvailableEvent(this.isWifiAvailable, this.isMobileAvailable);
        }

        @DebugLog
        @Subscribe
        // Listen for our data response.  The name of this method is meaningless, but should describe what's going on.
        public void getNetworkState(NetworkAvailableEvent networkAvailableEvent) {
            Log.w(TAG, String.format("Our NetworkListener was listening and got: %1$s", networkAvailableEvent.isNetworkAvailable));
            this.isWifiAvailable = networkAvailableEvent.isWifiAvailable;
            this.isMobileAvailable = networkAvailableEvent.isMobileAvailable;
            if (networkAvailableEvent.isNetworkAvailable) {
                EventBus.post(new GetApplicationSettingsRequest(getString(R.string.application_settings_url)));
            }
        }

    }

}
