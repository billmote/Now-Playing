package com.androidfu.foundation.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfu.foundation.R;
import com.androidfu.foundation.events.APIErrorEvent;
import com.androidfu.foundation.events.GetQuoteOfTheDayEvent;
import com.androidfu.foundation.model.QuoteOfTheDay;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.GoogleAnalyticsHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Subscribe;
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
    @InjectView(R.id.tv_quote)
    TextView mQuoteText;

    @DebugLog
    public static PlaceholderFragment newInstance() {
        PlaceholderFragment fragment = new PlaceholderFragment();
        return fragment;
    }

    @DebugLog
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

    @DebugLog
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

    @DebugLog
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

    @DebugLog
    @OnClick(R.id.button)
    public void fetchImage(View v) {
        mProgressBar.setVisibility(View.VISIBLE);
        mQuoteText.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);
        EventBus.post(new GetQuoteOfTheDayEvent(R.id.call_number_get_quote_of_the_day));
        Picasso.with(mHost)
                .load("http://goo.gl/G6RHYJ")
                .error(R.drawable.emoticon_sad)
                .fit()
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                        mQuoteText.setVisibility(View.VISIBLE);
                        mImageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        mProgressBar.setVisibility(View.GONE);
                        mImageView.setVisibility(View.VISIBLE);
                        Toast.makeText(mHost, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @DebugLog
    @Subscribe
    public void updateQuote(QuoteOfTheDay quoteOfTheDay) {
        mQuoteText.setText(quoteOfTheDay.getQuote());
    }

    @DebugLog
    @Subscribe
    public void apiErrorEvent(APIErrorEvent apiErrorEvent) {
        // Do nothing for now, but maybe we should put an ! icon on the ActionBar?
    }
}
