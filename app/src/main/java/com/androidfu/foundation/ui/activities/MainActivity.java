package com.androidfu.foundation.ui.activities;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.androidfu.foundation.R;
import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.ui.fragments.PlaceholderFragment;
import com.androidfu.foundation.ui.fragments.ReusableDialogFragment;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.GoogleAnalyticsHelper;
import com.androidfu.foundation.util.Log;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_APP_IS_DISABLED_FLAG;
import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_KILLSWITCH_MESSAGE;
import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_LAST_SEEN_MOTD_TIME_IN_MILLIS;
import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_MANDATORY_UPDATE_MESSAGE;
import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_MESSAGE_OF_THE_DAY;
import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_MOTD_LAST_UPDATED_TIME_IN_MILLIS;
import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_SHOW_UPDATE_NAG_FLAG;
import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_UPDATE_AVAILABLE_FLAG;
import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_UPDATE_NAG_MESSAGE;
import static com.androidfu.foundation.util.SharedPreferencesHelper.KEY_PREFS_UPDATE_REQUIRED_FLAG;
import static com.androidfu.foundation.util.SharedPreferencesHelper.getBoolean;
import static com.androidfu.foundation.util.SharedPreferencesHelper.getLong;
import static com.androidfu.foundation.util.SharedPreferencesHelper.getStringPreference;
import static com.androidfu.foundation.util.SharedPreferencesHelper.putLong;


public class MainActivity extends BaseActivity implements PlaceholderFragment.OnFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        EventBus.register(this);

        Tracker tracker = GoogleAnalyticsHelper.getInstance().getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        tracker.send(new HitBuilders.AppViewBuilder().build());

        PlaceholderFragment placeholderFragment = (PlaceholderFragment) getFragmentManager().findFragmentByTag(PlaceholderFragment.TAG);
        if (placeholderFragment == null) {
            placeholderFragment = PlaceholderFragment.newInstance();
            getFragmentManager().beginTransaction()
                    //.addToBackStack(PlaceholderFragment.TAG)
                    //.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_left, R.anim.slide_in_from_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, placeholderFragment, PlaceholderFragment.TAG)
                    .commit();
        }
    }

    @DebugLog
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.i(TAG, "SETTINGS");
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.unregister(this);
    }

    @Override
    public void onFragmentInteraction() {
        // Do something fancy here.
    }
}
