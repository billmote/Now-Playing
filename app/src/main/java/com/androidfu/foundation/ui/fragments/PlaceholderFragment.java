package com.androidfu.foundation.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidfu.foundation.R;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.GoogleAnalyticsHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction();
    }

    public static final String TAG = PlaceholderFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private Activity mHost;

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    @InjectView(R.id.imageView)
    ImageView mImageView;

    public static PlaceholderFragment newInstance() {
        PlaceholderFragment fragment = new PlaceholderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @DebugLog
    public PlaceholderFragment() {
        EventBus.register(this);
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
        ButterKnife.inject(this, rootView);

        Tracker tracker = GoogleAnalyticsHelper.getInstance().getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        tracker.setPage(TAG);
        tracker.send(new HitBuilders.AppViewBuilder().build());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mHost = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHost = null;
        mListener = null;
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.unregister(this);
    }

    @OnClick(R.id.button)
    public void fetchImage(View v) {
        mProgressBar.setVisibility(View.VISIBLE);
        Picasso.with(mHost)
                .load("http://goo.gl/G6RHYJ")
                .error(R.drawable.emoticon_sad)
                .fit()
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mHost, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}