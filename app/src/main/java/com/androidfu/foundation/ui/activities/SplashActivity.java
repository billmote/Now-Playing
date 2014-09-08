package com.androidfu.foundation.ui.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidfu.foundation.FoundationApplication;
import com.androidfu.foundation.R;
import com.androidfu.foundation.events.APIOkEvent;
import com.androidfu.foundation.events.ApiErrorEvent;
import com.androidfu.foundation.events.GetApplicationSettingsEvent;
import com.androidfu.foundation.localcache.AppSettingsLocalStorageHandler;
import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.ui.fragments.ReusableDialogFragment;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.Log;
import com.androidfu.foundation.util.SharedPreferencesHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;

public class SplashActivity extends Activity implements ReusableDialogFragment.ReusableDialogListener {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private boolean mAlreadyHandledNeutralResult;

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        EventBus.register(this);

        if (savedInstanceState == null) {
            EventBus.post(new GetApplicationSettingsEvent(R.id.call_number_retrieve_application_settings));
            mProgressBar.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 3000);
        } else {
            // App was already running.  Skip any artificial delay.
            carryOn();
        }

    }

    @DebugLog
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        EventBus.unregister(this);
    }

    /**
     * Display any relevant messaging to the user, prompt them to upgrade if available, or block
     * execution of the application based on the result of the data collected in ApplicationSettings
     * from our PaaS provider.
     */
    @DebugLog
    @Subscribe
    public void interruptTheUser(APIOkEvent apiOkEvent) {
        AppSettingsLocalStorageHandler appSettingsDBHandler = new AppSettingsLocalStorageHandler(this);

        mProgressBar.setVisibility(View.GONE);

        DialogFragment dialogFragment;
        ApplicationSettings appSettings = null;
        boolean interruptedTheUser = false;

        try {
            appSettings = appSettingsDBHandler.getCurrentApplicationSettings();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (appSettings == null) {
            // We're in a bad spot.  Need to quit.
            Toast.makeText(this, "APPLICATION ERROR -- PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (appSettings.isApp_disabled()) {
            /* The application has been disabled via the kill-switch */
            Log.wtf(TAG, "The developer has decided that this application should not be run.");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_temporarily_disabled), appSettings.getKill_switch_message_text(), null, null, getString(R.string.dialog_button_quit), null);
            displayDialogFragment(dialogFragment, false);

            interruptedTheUser = true;
        }

        if (!interruptedTheUser && appSettings.getLow_watermark_version_number() > FoundationApplication.APP_VERSION_CODE) {
            /* The developer has decided that your version should no longer be in use. */
            Log.wtf(TAG, "Displaying the mandatory update dialog.");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_update_required), appSettings.getMandatory_update_message_text(), getString(R.string.dialog_button_update), null, getString(R.string.dialog_button_quit), null);
            displayDialogFragment(dialogFragment, false);

            interruptedTheUser = true;
        }

        if (!interruptedTheUser && appSettings.isUpg_nag_enabled() && appSettings.getProduction_version_number() > FoundationApplication.APP_VERSION_CODE) {
            /* Your version is older than the current version.  Display an update nag screen with a list of new features. */
            Log.i(TAG, "Displaying the update nag.");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_update_available), appSettings.getUpdate_nag_message_text(), getString(R.string.dialog_button_update), getString(R.string.dialog_button_not_now), null, null);
            displayDialogFragment(dialogFragment, true);
            // Display update icon on the options menu
            invalidateOptionsMenu();

            interruptedTheUser = true;
        } else {
            // Hide the update icon on the options menu
            invalidateOptionsMenu();
        }

        if (!interruptedTheUser && SharedPreferencesHelper.getLong(SharedPreferencesHelper.KEY_PREFS_LAST_SEEN_MOTD_TIME_IN_MILLIS, 0) < appSettings.getLast_updated_on()) {
            /* You haven't seen this MOTD */
            // getBoolean(!KEY_PREFS_MOTD_SEEN_FLAG, false) &&
            Log.i(TAG, "Displaying the MOTD.");
            SharedPreferencesHelper.putLong(SharedPreferencesHelper.KEY_PREFS_LAST_SEEN_MOTD_TIME_IN_MILLIS, System.currentTimeMillis());
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_message_of_the_day), appSettings.getMessage_of_the_day_text(), null, getString(R.string.dialog_button_ok), null, null);
            displayDialogFragment(dialogFragment, true);

            interruptedTheUser = true;
        }

        if (!interruptedTheUser) {
            carryOn();
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
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.google.android.googlequicksearchbox")));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox")));
        }
    }

    @DebugLog
    @Override
    public void handleNeutralResult() {
        // Because the user can press outside the dialog to dismiss as well as pressing the
        // "OKAY" button we need to make sure we only end up with 1 activity.
        if (!mAlreadyHandledNeutralResult) {
            mAlreadyHandledNeutralResult = true;
            carryOn();
        }
    }

    @DebugLog
    @Override
    public void handleNegativeResult() {
        // Exit our app
        finish();
    }

    @DebugLog
    private void carryOn() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @DebugLog
    @Subscribe
    public void onApiErrorEvent(ApiErrorEvent errorEvent) {

        mProgressBar.setVisibility(View.GONE);

        if (errorEvent.isNetworkError()) {
            Toast.makeText(this, R.string.error_no_network, Toast.LENGTH_SHORT).show();
            interruptTheUser(null);
            return;
        }
        switch (errorEvent.getHttpStatusCode()) {
            case GetApplicationSettingsEvent.ERROR_NOT_FOUND:
            default:
                Toast.makeText(this, R.string.error_server_error, Toast.LENGTH_SHORT).show();
                interruptTheUser(null);
        }
    }

    @DebugLog
    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @DebugLog
    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

}