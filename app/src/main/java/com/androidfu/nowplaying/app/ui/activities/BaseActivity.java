package com.androidfu.nowplaying.app.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.androidfu.nowplaying.app.BuildConfig;
import com.androidfu.nowplaying.app.R;
import com.androidfu.nowplaying.app.events.CaptivePortalAuthReqEvent;
import com.androidfu.nowplaying.app.util.DebugUtils;
import com.androidfu.nowplaying.app.util.EventBus;
import com.androidfu.nowplaying.app.util.Log;
import com.androidfu.nowplaying.app.util.SharedPreferencesHelper;
import com.squareup.otto.Subscribe;

import hugo.weaving.DebugLog;

@DebugLog
public class BaseActivity extends Activity {

    public static final String TAG = "BaseActivity";

    @Override
    protected void onStart() {
        super.onStart();
        // Wake the device and show our activity
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Wakeup!");
            DebugUtils.riseAndShine(this);
        }
        EventBus.register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG && menu.findItem(R.id.force_crash) == null) {
            // Only show these options for developer builds
            menu.add(R.id.developer_preference_group, R.id.force_crash, Menu.NONE, R.string.developer_action_force_crash);
            menu.add(R.id.developer_preference_group, R.id.reset_prefs, Menu.NONE, R.string.developer_action_reset_application_preferences);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    protected void onStop() {
        EventBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onCaptivePortalEvent(CaptivePortalAuthReqEvent event) {
        Toast.makeText(this, "You need to login.", Toast.LENGTH_LONG).show();
    }
}
