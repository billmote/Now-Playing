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


public class MainActivity extends BaseActivity implements ReusableDialogFragment.ReusableDialogListener, PlaceholderFragment.OnFragmentInteractionListener {

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

    /**
     * Display any relevant messaging to the user, prompt them to upgrade if available, or block
     * execution of the application based on the result of the data collected in ApplicationSettings
     * from our PaaS provider.
     */
    @DebugLog
    @Subscribe
    public void interruptTheUser(ApplicationSettings applicationSettings) {

        DialogFragment dialogFragment;

        // Static import of SharedPreferencesHelper used to reduce typing and make code easier to read.
        if (getBoolean(KEY_PREFS_APP_IS_DISABLED_FLAG, false)) {
            /* The application has been disabled via the kill-switch @ parse */
            Log.i(TAG, "The developer has decided that this application should not be run.");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_temporarily_disabled), getStringPreference(KEY_PREFS_KILLSWITCH_MESSAGE, getString(R.string.error_message_something_went_wrong)), null, null, getString(R.string.dialog_button_quit), null);
            displayDialogFragment(dialogFragment, false);

            return;
        }

        if (getBoolean(KEY_PREFS_UPDATE_REQUIRED_FLAG, false)) {
            /* The developer has decided that your version should no longer be in use. */
            Log.i(TAG, "Displaying the mandatory update dialog.");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_update_required), getStringPreference(KEY_PREFS_MANDATORY_UPDATE_MESSAGE, getString(R.string.error_message_something_went_wrong)), getString(R.string.dialog_button_update), null, getString(R.string.dialog_button_quit), null);
            displayDialogFragment(dialogFragment, false);

            return;
        }

        if (getBoolean(KEY_PREFS_SHOW_UPDATE_NAG_FLAG, true) && getBoolean(KEY_PREFS_UPDATE_AVAILABLE_FLAG, false)) {
            /* Your version is older than the current version.  Display an update nag screen with a list of new features. */
            Log.i(TAG, "Displaying the update nag.");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_update_available), getStringPreference(KEY_PREFS_UPDATE_NAG_MESSAGE, getString(R.string.error_message_something_went_wrong)), getString(R.string.dialog_button_update), getString(R.string.dialog_button_not_now), null, null);
            displayDialogFragment(dialogFragment, true);
            invalidateOptionsMenu();

            return;
        } else {
            invalidateOptionsMenu();
        }

        if (getLong(KEY_PREFS_LAST_SEEN_MOTD_TIME_IN_MILLIS, 0) < getLong(KEY_PREFS_MOTD_LAST_UPDATED_TIME_IN_MILLIS, 0)) {
            /* You haven't seen this MOTD */
            // getBoolean(!KEY_PREFS_MOTD_SEEN_FLAG, false) &&
            Log.i(TAG, "Displaying the MOTD.");
            putLong(KEY_PREFS_LAST_SEEN_MOTD_TIME_IN_MILLIS, System.currentTimeMillis());
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_message_of_the_day), getStringPreference(KEY_PREFS_MESSAGE_OF_THE_DAY, getString(R.string.error_message_something_went_wrong)), null, getString(R.string.dialog_button_ok), null, null);
            displayDialogFragment(dialogFragment, true);

            return;
        }
    }

    /**
     * DRY (Don't Repeat Yourself) -- just a helper method so I'm not repeating these lines of
     * code.
     *
     * @param dialogFragment the DialogFragment to hold the data being displayed to the user.
     * @param isCancelable   whether or not the user should be able to cancel the dialog without selecting an
     *                       option.
     */
    @DebugLog
    private void displayDialogFragment(DialogFragment dialogFragment, boolean isCancelable) {
        dialogFragment.setCancelable(isCancelable);
        if (getFragmentManager() != null && getFragmentManager().findFragmentByTag(ReusableDialogFragment.TAG) == null) {
            dialogFragment.show(getFragmentManager(), ReusableDialogFragment.TAG);
        }
    }

    @DebugLog
    @Override
    public void handlePositiveResult() {
        // Positive will only be available as an "update" option
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.androidfu.absolution.app")));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.androidfu.absolution.app")));
        }
    }

    @DebugLog
    @Override
    public void handleNeutralResult() {
        // Nothing to see here.  Let the user carry on.
    }

    @DebugLog
    @Override
    public void handleNegativeResult() {
        // Exit our app
        finish();
    }

    @Override
    public void onFragmentInteraction() {
        // Do something fancy here.
    }
}
