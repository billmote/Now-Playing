package com.androidfu.foundation.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidfu.foundation.R;
import com.androidfu.foundation.api.GetApplicationSettingsRequest;
import com.androidfu.foundation.api.GetNetworkImageRequest;
import com.androidfu.foundation.api.GetNetworkImageResponse;
import com.androidfu.foundation.events.NetworkAvailableEvent;
import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.Log;
import com.androidfu.foundation.util.NetworkStatus;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public static final String TAG = PlaceholderFragment.class.getSimpleName();

    @InjectView(R.id.button)
    Button mButton;
    @InjectView(R.id.imageView)
    ImageView mImageView;
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;

    private String mUrl = "https://raw.githubusercontent.com/androidfu/foundation_api/master/v1.0/2014-07-21%2010.35.42.jpg";

    @DebugLog
    public PlaceholderFragment() {
        // Register our Fragment with the Otto Bus singleton
        EventBus.register(this);
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);

        // Inject our Views
        ButterKnife.inject(this, rootView);

        NetworkStatus.checkInternetConnection(getActivity(), new NetworkStatus.NetworkStatusListener() {

            @DebugLog
            @Override
            public void onNetworkAvailable() {
                // Go get some data from the network by posting a "Request" object to the Bus.
                EventBus.post(new GetApplicationSettingsRequest(getString(R.string.application_settings_url)));
            }

            @DebugLog
            @Override
            public void onNetworkNotAvailable() {
                // display an alert message to the user.
                Log.e(TAG, "No network available.");
            }
        });

        return rootView;
    }

    // Let ButterKnife remove some boilerplate code
    @DebugLog
    @OnClick(R.id.button)
    public void fetchImage() {
        mProgressBar.setVisibility(View.VISIBLE);
        // Go get some data from the network by posting a "Request" object to the Bus.
        EventBus.post(new GetNetworkImageRequest(mImageView, mUrl));
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
    public void hideProgressSpinner(GetNetworkImageResponse response) {
        mProgressBar.setVisibility(View.GONE);
    }

    @DebugLog
    @Subscribe
    public void updateAndEnableButton(ApplicationSettings applicationSettings) {
        mProgressBar.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(applicationSettings.messageOfTheDay)) {
            mButton.setText(applicationSettings.messageOfTheDay);
        }
    }

    @DebugLog
    @Subscribe
    public void setButtonState(NetworkAvailableEvent networkAvailableEvent) {
        if (mButton == null) {
            return;
        }
        if (networkAvailableEvent.isNetworkAvailable) {
            mButton.setEnabled(true);
            return;
        }
        mButton.setEnabled(false);
    }

}
