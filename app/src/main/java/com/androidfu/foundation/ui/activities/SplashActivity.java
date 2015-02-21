package com.androidfu.foundation.ui.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidfu.foundation.BuildConfig;
import com.androidfu.foundation.FoundationApplication;
import com.androidfu.foundation.R;
import com.androidfu.foundation.events.APIErrorEvent;
import com.androidfu.foundation.events.APIOkEvent;
import com.androidfu.foundation.events.application.GetApplicationSettingsEvent;
import com.androidfu.foundation.localcache.AppSettingsLocalStorageHandler;
import com.androidfu.foundation.model.application.ApplicationSettings;
import com.androidfu.foundation.DebugUtils;
import com.androidfu.foundation.ui.fragments.ReusableDialogFragment;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.Log;
import com.androidfu.foundation.util.SharedPreferencesHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;

@DebugLog
public class SplashActivity extends Activity implements ReusableDialogFragment.ReusableDialogListener {

    public static final String TAG = SplashActivity.class.getSimpleName();

    private static final String KEY_BUNDLE_USER_INTERRUPTED_STATE = "is_user_interrupted";
    private static final String KEY_BUNDLE_ERROR_MESSAGE = "error_message";
    private static final String KEY_BUNDLE_CATASTROPHIC_FAILURE = "is_catastrophic_failure";
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    /**
     * Dismissable dialogs call handleNeutralResult() twice.  Once for the actual touch that
     * dismissed the dialog and once as a result of onDismiss().  Why?  Who knows, but this flag
     * will keep things square.
     */
    private boolean mAlreadyHandledDialogResult;
    /**
     * Keep track of whether or not we've interrupted the user.  This is important when determining
     * which state to put the app in in onResume().  We also need to track this flag between
     * view creations so they cannot bypass a hard-stop dialog like the kill switch or mandatory
     * update.
     */
    private boolean mInterruptedTheUser;
    private String mErrorMessage = "IDK";
    private boolean mCatastrophicFailure;
    private DialogFragment mInterruptTheUserDialog;
    private final Handler mHandler = new Handler();
    private final Runnable mSplashRunnable = new SplashRunnable();
    private boolean mHideMotd;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        // Wake the device and show our activity
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Wakeup!");
            // Calling this from your launcher activity is enough, but I needed a good example spot ;)
            DebugUtils.riseAndShine(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        if (savedInstanceState == null) {
            mInterruptedTheUser = true; // Careful, this is used in onResume()
            EventBus.post(new GetApplicationSettingsEvent(R.id.api_call_get_application_settings));
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            try {
                mInterruptedTheUser = savedInstanceState.getBoolean(KEY_BUNDLE_USER_INTERRUPTED_STATE, true);
                mErrorMessage = savedInstanceState.getString(KEY_BUNDLE_ERROR_MESSAGE);
                mCatastrophicFailure = savedInstanceState.getBoolean(KEY_BUNDLE_CATASTROPHIC_FAILURE);
            } catch (Exception e) {
                Log.e(TAG, "We failed to reassign our fields from our state bundle.", e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.register(this);
        mHandler.postDelayed(mSplashRunnable, 3000);
    }

    /**
     * We're going to display our branding here, but the process can be expedited by our API
     * response coming back quickly as we'll end up in interruptTheUser() via Otto.
     */
    @DebugLog
    private class SplashRunnable implements Runnable {
        @Override
        public void run() {
            /**
             * We're setting a flag to handle a couple of states:
             *
             * 1. If the app is not running we
             * want to go through interruptTheUser() so the flag is set to true in onCreate().  This
             * is done if our bundle is not null.  We then set it back to false and iterate over our
             * logic in interruptTheUser().
             *
             * 2. If the user is sent to the play store for an update then we need them to go back
             * through our interruptTheUser() method as the application state may be set to "disabled"
             * or have the mandatory update flag set.  interruptTheUser() will set the flag to true
             * as required.
             *
             * The only way to get to carryOn() is if the app was already running and we did not
             * interrupt the user during the original execution of SplashActivity.
             */
            if (!mInterruptedTheUser) {
                carryOn();
            } else {
                interruptTheUser(null);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.unregister(this);
        mHandler.removeCallbacks(mSplashRunnable);
    }

    @Override
    protected void onSaveInstanceState(@Nonnull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_BUNDLE_USER_INTERRUPTED_STATE, mInterruptedTheUser);
        outState.putString(KEY_BUNDLE_ERROR_MESSAGE, mErrorMessage);
        outState.putBoolean(KEY_BUNDLE_CATASTROPHIC_FAILURE, mCatastrophicFailure);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    /**
     * Display any relevant messaging to the user, prompt them to upgrade if available, or block
     * execution of the application based on the result of the data collected in ApplicationSettings
     * from our PaaS provider.
     */
    @Subscribe
    public void interruptTheUser(APIOkEvent event) {
        if (mInterruptTheUserDialog != null) {
            /**
             * If we're already displaying a dialog then don't re-run through this method.
             */
            return;
        }
        if (event != null && event.getCallNumber() != R.id.api_call_get_application_settings) {
            /**
             * We call this method passing in a null argument when we're coming from onResume() or
             * from an APIErrorEvent.  So, if we have a non-null "event" and the event was the result
             * of a call made to GetApplicationSettingsEvent then we're good to continue.
             *
             * APIOkEvent is a hollow event carrying little more than an int representing the call
             * for which it is responding "success."
             */
            return;
        } else if ((event == null) && SharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_PREFS_FIRST_RUN, true)) {
            /**
             * If we had a null event we know we're coming from onResume() or an APIErrorEvent,
             * but lets check to see if this is a first run of the application and set a flag.  We
             * need a database copy of our ApplicationSettings to continue past this point in the
             * method or we'll throw a catastrophic failure.
             */
            mHideMotd = true;
            return;
        }

        DialogFragment dialogFragment;
        ApplicationSettings appSettings = null;
        mInterruptedTheUser = false;
        AppSettingsLocalStorageHandler appSettingsDBHandler = new AppSettingsLocalStorageHandler(this);
        try {
            appSettings = appSettingsDBHandler.getCurrentApplicationSettings();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mProgressBar.setVisibility(View.GONE);

        if (appSettings == null) {
            /* This is a bad spot to be in. */
            Log.wtf(TAG, "Catastrophic Failure: ApplicationSettings == null");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_catastrophic_failure), String.format(getString(R.string.dialog_body_catastrophic_failure), mErrorMessage), null, null, getString(R.string.dialog_button_quit), null);
            displayDialogFragment(dialogFragment, false);
            mCatastrophicFailure = true;
            mInterruptedTheUser = true;
            EventBus.unregister(this);
            return;
        }

        if (appSettings.isAppDisabled()) {
            /* The application has been disabled via the kill-switch */
            Log.wtf(TAG, "The developer has decided that this application should not be run.");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_temporarily_disabled), appSettings.getKillSwitchMessageText(), null, null, getString(R.string.dialog_button_quit), null);
            displayDialogFragment(dialogFragment, false);

            mInterruptedTheUser = true;
        }

        if (!mInterruptedTheUser && appSettings.getLwmVersionNum() > FoundationApplication.APP_VERSION_CODE) {
            /* The developer has decided that your version should no longer be in use. */
            Log.wtf(TAG, "Displaying the mandatory update dialog.");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_update_required), appSettings.getMandatoryUpdateMessageText(), getString(R.string.dialog_button_update), null, getString(R.string.dialog_button_quit), null);
            displayDialogFragment(dialogFragment, false);

            mInterruptedTheUser = true;
        }

        if (!mInterruptedTheUser && appSettings.isUpdateNagEnabled() && appSettings.getProdVersionNum() > FoundationApplication.APP_VERSION_CODE) {
            /* Your version is older than the current version.  Display an update nag screen with a list of new features. */
            Log.i(TAG, "Displaying the update nag.");
            dialogFragment = ReusableDialogFragment.newInstance(getString(R.string.dialog_title_update_available), appSettings.getChangeLog(), getString(R.string.dialog_button_update), getString(R.string.dialog_button_not_now), null, null);
            displayDialogFragment(dialogFragment, false);

            // Display update icon on the options menu
            invalidateOptionsMenu();

            mInterruptedTheUser = true;

        } else {
            // Hide the update icon on the options menu
            invalidateOptionsMenu();
        }

        /**
         * If ...
         * motdFrequency == 0, Message of the Day is disabled, return false.
         * motdFrequency == -1, Message of the Day should always be shown, return true.
         * Otherwise do the math and honor the value returned in ApplicationSettings
         */
        long motdFrequency = appSettings.getMotdFrequency();
        boolean showMotd = (motdFrequency == 0 ? false : motdFrequency == -1 ? true : System.currentTimeMillis() - SharedPreferencesHelper.getLong(SharedPreferencesHelper.KEY_PREFS_LAST_SEEN_MOTD_TIME_IN_MILLIS, 0) > motdFrequency);
        if (!mInterruptedTheUser && showMotd && !mHideMotd) {
            /* Show the MOTD */
            Log.i(TAG, "Displaying the MOTD.");
            SharedPreferencesHelper.putLong(SharedPreferencesHelper.KEY_PREFS_LAST_SEEN_MOTD_TIME_IN_MILLIS, System.currentTimeMillis());
            dialogFragment = ReusableDialogFragment.newInstance(appSettings.getMotdTitle(), appSettings.getMotdMessageText(), null, getString(R.string.dialog_button_ok), null, null);
            displayDialogFragment(dialogFragment, false);

            mInterruptedTheUser = true;
        }

        if (!mInterruptedTheUser) {
            /**
             * Only carryOn() if we don't have a dialog visible.
             */
            carryOn();
        }
    }

    private void carryOn() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }

        SharedPreferencesHelper.putBoolean(SharedPreferencesHelper.KEY_PREFS_FIRST_RUN, false);

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * DRY (Don't Repeat Yourself) -- just a helper method so I'm not repeating these lines of
     * code.
     *
     * @param dialogFragment the DialogFragment to hold the data being displayed to the user.
     * @param isCancelable   whether or not the user should be able to cancel the dialog without selecting an
     *                       option.
     */
    private void displayDialogFragment(DialogFragment dialogFragment, boolean isCancelable) {
        mAlreadyHandledDialogResult = false;
        mInterruptTheUserDialog = dialogFragment;
        dialogFragment.setCancelable(isCancelable);
        if (getFragmentManager().findFragmentByTag(ReusableDialogFragment.TAG) == null) {
            dialogFragment.show(getFragmentManager(), ReusableDialogFragment.TAG);
        }
    }

    @Override
    public void handlePositiveResult() {
        mAlreadyHandledDialogResult = true;
        mInterruptTheUserDialog = null;
        // Positive will only be available as an "update" option
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_market_url))));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_http_url))));
        }
    }

    @Override
    public void handleNeutralResult() {
        // Because the user can press outside the dialog to dismiss as well as pressing the
        // "OKAY" button we need to make sure we only end up with 1 activity.
        mInterruptTheUserDialog = null;
        if (!mAlreadyHandledDialogResult) {
            mAlreadyHandledDialogResult = true;
            carryOn();
        }
    }

    @Override
    public void handleNegativeResult() {
        mAlreadyHandledDialogResult = true;
        mInterruptTheUserDialog = null;
        // Exit our app
        if (mCatastrophicFailure) {
            SharedPreferencesHelper.clear();
        }
        finish();
    }

    @Subscribe
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored") // We're going to die gracefully
    public void onApiErrorEvent(APIErrorEvent error) {

        mProgressBar.setVisibility(View.GONE);
        mErrorMessage = error.getError().getMessage();

        if (error.isNetworkError()) {
            try {
                Log.e(TAG, String.format("Network Error for call %1$s: ", getResources().getResourceEntryName(error.getCallNumber())), error.getError());
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, R.string.error_no_network, Toast.LENGTH_SHORT).show();
            interruptTheUser(null);
            return;
        }
        switch (error.getHttpStatusCode()) {
            case GetApplicationSettingsEvent.ERROR_NOT_FOUND:
            default:
                Log.e(TAG, String.format("Failed with HTTP Status code: %1$d", error.getHttpStatusCode()));
                Toast.makeText(this, R.string.error_server_error, Toast.LENGTH_SHORT).show();
                interruptTheUser(null);
        }
        switch (error.getCallNumber()) {
            case R.id.api_call_get_application_settings:
                Toast.makeText(this, R.string.error_application_settings, Toast.LENGTH_SHORT).show();
                Log.e(TAG, getString(R.string.error_application_settings), error.getError());
                break;
            default:
                try {
                    Log.wtf(TAG, String.format("Unhandled call %1$s error in our class: ", getResources().getResourceEntryName(error.getCallNumber())), error.getError());
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
        }
        interruptTheUser(null);
    }
}