package com.androidfu.foundation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.androidfu.foundation.BuildConfig;
import com.androidfu.foundation.R;
import com.androidfu.foundation.util.Log;
import com.androidfu.foundation.util.SharedPreferencesHelper;

import hugo.weaving.DebugLog;

public class BaseActivity extends Activity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
    }

    @DebugLog
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG && menu.findItem(R.id.force_crash) == null) {
            // Only show these options for developer builds
            menu.add(R.id.developer_preference_group, R.id.force_crash, Menu.NONE, R.string.developer_action_force_crash);
            menu.add(R.id.developer_preference_group, R.id.reset_prefs, Menu.NONE, R.string.developer_action_reset_application_preferences);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @DebugLog
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
}
