package com.androidfu.foundation.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.androidfu.foundation.R;
import com.androidfu.foundation.api.GetApplicationSettingsRequest;
import com.androidfu.foundation.events.NetworkAvailableEvent;
import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.util.EventBus;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public static final String TAG = PlaceholderFragment.class.getSimpleName();

    @InjectView(R.id.button)
    Button mButton;
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    private Activity mHost;

    @DebugLog
    public PlaceholderFragment() {
        // Register our Fragment with the Otto Bus singleton
        EventBus.register(this);
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHost = null;
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.unregister(this);
    }

    @DebugLog
    @Subscribe
    public void showProgressSpinner(GetApplicationSettingsRequest request) {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @DebugLog
    @Subscribe
    public void updateAndEnableButton(ApplicationSettings applicationSettings) {
        mProgressBar.setVisibility(View.GONE);
        mButton.setEnabled(true);
        if (!TextUtils.isEmpty(applicationSettings.messageOfTheDay)) {
            mButton.setText(applicationSettings.messageOfTheDay);
        } else {
            mButton.setText("No MOTD Today");
        }
    }
}
