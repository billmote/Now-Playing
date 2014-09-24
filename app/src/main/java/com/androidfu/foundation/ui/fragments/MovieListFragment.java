package com.androidfu.foundation.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfu.foundation.R;
import com.androidfu.foundation.events.APIErrorEvent;
import com.androidfu.foundation.events.movies.GetMoviesEvent;
import com.androidfu.foundation.model.movies.Movie;
import com.androidfu.foundation.model.movies.Movies;
import com.androidfu.foundation.ui.adapters.MovieAdapter;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.GoogleAnalyticsHelper;
import com.androidfu.foundation.util.Log;
import com.androidfu.foundation.util.SoundManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private int mSoundID;

    public interface OnFragmentInteractionListener {
        @DebugLog
        public void onMovieSelected(Movie movie);
    }

    public static final String TAG = MovieListFragment.class.getSimpleName();

    private static final String KEY_BUNDLE_MOVIES = "movies";
    private static final String KEY_BUNDLE_FIRST_VISIBLE_ITEM = "first_visible_movie";
    private static final String KEY_BUNDLE_SCROLL_OFFSET = "scroll_offset";

    private int mScrollPosition = 0;
    private int mScrollOffset = 0;
    private List<Movie> mMovies;
    private OnFragmentInteractionListener mListener;
    private Activity mHost;

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    @InjectView(android.R.id.list)
    ListView mMovieListView;
    @InjectView(android.R.id.empty)
    TextView mEmptyTextView;
    @InjectView(R.id.button)
    Button mFetchMoviesBtn;

    @DebugLog
    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @DebugLog
    public MovieListFragment() {
        // Required empty constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
        ButterKnife.inject(this, rootView);

        Tracker tracker = GoogleAnalyticsHelper.getInstance().getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        tracker.setPage(TAG);
        tracker.send(new HitBuilders.AppViewBuilder().build());

        mMovieListView.setEmptyView(mEmptyTextView);
        mMovieListView.setOnItemClickListener(this);

        return rootView;
    }

    @DebugLog
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_BUNDLE_MOVIES)) {
                mMovies = Parcels.unwrap(savedInstanceState.getParcelable(KEY_BUNDLE_MOVIES));
            }
            if (savedInstanceState.containsKey(KEY_BUNDLE_FIRST_VISIBLE_ITEM) && savedInstanceState.containsKey(KEY_BUNDLE_SCROLL_OFFSET)) {
                mScrollPosition = savedInstanceState.getInt(KEY_BUNDLE_FIRST_VISIBLE_ITEM, 0);
                mScrollOffset = savedInstanceState.getInt(KEY_BUNDLE_SCROLL_OFFSET, 0);
            }
        }
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
        EventBus.register(this);
        if (mMovies != null && !mMovies.isEmpty()) {
            /**
             * If we have a movie list use it rather than making the user re-fetch
             * it from our API.
             */
            showMovieList(null);
        }
    }

    @DebugLog
    @Override
    public void onPause() {
        super.onPause();
        EventBus.unregister(this);
        /**
         * In addition to grabbing the top visible item also store the view offset
         * so we can truly keep our view exactly where it was.
         */
        mScrollPosition = mMovieListView.getFirstVisiblePosition();
        View view = mMovieListView.getChildAt(0);
        mScrollOffset = (view == null) ? 0 : view.getTop();
    }

    @DebugLog
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /**
         * Take a look at the model objects that comprise "movies" and you'll see
         * why Parceler is so fantastic.  1 annotation in each model object and we
         * get to use Android's amazingly fast Parcelable.
         */
        outState.putParcelable(KEY_BUNDLE_MOVIES, Parcels.wrap(mMovies));
        outState.putInt(KEY_BUNDLE_FIRST_VISIBLE_ITEM, mScrollPosition);
        outState.putInt(KEY_BUNDLE_SCROLL_OFFSET, mScrollOffset);
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
    }

    @DebugLog
    @OnClick(R.id.button)
    public void getMovieList(View v) {
        mScrollPosition = mScrollOffset = 0;
        mProgressBar.setVisibility(View.VISIBLE);
        EventBus.post(new GetMoviesEvent(R.id.api_call_get_movies));
    }

    @DebugLog
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onMovieSelected((Movie) parent.getItemAtPosition(position));
    }

    @DebugLog
    @Subscribe
    public void showMovieList(Movies movies) {
        mProgressBar.setVisibility(View.GONE);
        mFetchMoviesBtn.setText(R.string.btn_refresh);
        if (movies != null) {
            /**
             * We pass null from onResume() if we already have a list of movies otherwise we
             * should be getting a response from our API call and we'll populate the movies
             * here.
             */
            mSoundID = SoundManager.playSound(R.raw.success);
            mMovies = movies.getMovies();
        }
        final MovieAdapter movieAdapter = new MovieAdapter(mHost, R.layout.listview_movie_row, mMovies);
        mMovieListView.setAdapter(movieAdapter);
        mMovieListView.setSelectionFromTop(mScrollPosition, mScrollOffset);
    }

    @DebugLog
    @Subscribe
    public void apiErrorEvent(APIErrorEvent error) {
        // Do nothing for now, but maybe we should put an ! icon on the ActionBar?
        mProgressBar.setVisibility(View.GONE);
        SoundManager.stopSound(mSoundID);
        mSoundID = SoundManager.playSound(R.raw.fail);
        if (error.isNetworkError()) {
            try {
                // Surrounded with try/catch because too many things can throw an NPE here.
                Log.wtf(TAG, String.format("Network Error for call %1$s: ", getResources().getResourceEntryName(error.getCallNumber())), error.getError());
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        switch (error.getHttpStatusCode()) {
            default:
                Log.e(TAG, String.format("Failed with HTTP Status code: %1$d", error.getHttpStatusCode()));
        }
        switch (error.getCallNumber()) {
            case R.id.api_call_get_movies:
                Toast.makeText(mHost, "Failed to get our list of movies.", Toast.LENGTH_SHORT).show();
                return;
            default:
                try {
                    // Surrounded with try/catch because too many things can throw an NPE here.
                    Log.wtf(TAG, String.format("Unhandled call %1$s error in our class: ", getResources().getResourceEntryName(error.getCallNumber())), error.getError());
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
        }
    }

}
