package com.androidfu.nowplaying.app.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfu.nowplaying.app.R;
import com.androidfu.nowplaying.app.events.APIErrorEvent;
import com.androidfu.nowplaying.app.events.movies.GetMoviesEvent;
import com.androidfu.nowplaying.app.model.movies.Movie;
import com.androidfu.nowplaying.app.model.movies.Movies;
import com.androidfu.nowplaying.app.ui.adapters.MovieAdapter;
import com.androidfu.nowplaying.app.util.EventBus;
import com.androidfu.nowplaying.app.util.GoogleAnalyticsHelper;
import com.androidfu.nowplaying.app.util.Log;
import com.androidfu.nowplaying.app.util.SoundManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

/**
 * Display a list of Box Office Movies from Rotten Tomatoes
 */
@DebugLog
public class MovieListFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    public static final String TAG = MovieListFragment.class.getSimpleName();

    private static final int DEFAULT_PAGE_SIZE = 16;
    private static final int MAX_PAGE_SIZE = 50;
    private static final String KEY_BUNDLE_PAGE_NUMBER = "page_number";
    private static final String KEY_BUNDLE_MOVIES = "movies";
    private static final String KEY_BUNDLE_FIRST_VISIBLE_ITEM = "first_visible_movie";
    private static final String KEY_BUNDLE_SCROLL_OFFSET = "scroll_offset";
    private static final String KEY_BUNDLE_TOTAL_MOVIES = "total_num_movies";

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    @InjectView(android.R.id.list)
    ListView mMovieListView;
    @InjectView(android.R.id.empty)
    TextView mEmptyTextView;
    @InjectView(R.id.button)
    Button mFetchMoviesBtn;

    private TextView mFooterTextView;
    private int mSoundID;
    private int mPageNumber = 1;
    private int mTotalMovies = 0;
    private int mScrollPosition = 0;
    private int mScrollOffset = 0;
    private List<Movie> mMovies = new ArrayList<>();
    private MovieAdapter mMovieAdapter;
    private OnFragmentInteractionListener mListener;
    private Activity mHost;
    private boolean mEndOfList = false;

    public MovieListFragment() {
        // Required empty constructor
    }

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movielist, container, false);
        ButterKnife.inject(this, rootView);

        Tracker tracker = GoogleAnalyticsHelper.getInstance().getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        tracker.setPage(TAG);
        tracker.send(new HitBuilders.AppViewBuilder().build());

        mMovieListView.setEmptyView(mEmptyTextView);
        mMovieListView.setOnItemClickListener(this);
        mMovieListView.setOnScrollListener(this);

        View mListFooterView = inflater.inflate(R.layout.footer_layout, mMovieListView, false);
        mFooterTextView = (TextView) mListFooterView.findViewById(R.id.tv_footer_text);
        setFooterText();
        mMovieListView.addFooterView(mListFooterView);

        return rootView;
    }

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
            if (savedInstanceState.containsKey(KEY_BUNDLE_PAGE_NUMBER)) {
                mPageNumber = savedInstanceState.getInt(KEY_BUNDLE_PAGE_NUMBER, 1);
            }
            if (savedInstanceState.containsKey(KEY_BUNDLE_TOTAL_MOVIES)) {
                mTotalMovies = savedInstanceState.getInt(KEY_BUNDLE_TOTAL_MOVIES, 0);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.register(this);
        if (!mMovies.isEmpty()) {
            /**
             * If we have a movie list use it rather than making the user re-fetch
             * it from our API.
             */
            showMovieList(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.unregister(this);
        recordScrollPosition();
    }

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
        outState.putInt(KEY_BUNDLE_PAGE_NUMBER, mPageNumber);
        outState.putInt(KEY_BUNDLE_TOTAL_MOVIES, mTotalMovies);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHost = null;
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.button)
    public void getMovieList(View v) {
        int pageLimit = DEFAULT_PAGE_SIZE;
        if (!mMovies.isEmpty()) {
            if (--mPageNumber > 0) {
                pageLimit = mPageNumber * DEFAULT_PAGE_SIZE;
            } else {
                pageLimit = DEFAULT_PAGE_SIZE;
            }
            if (mTotalMovies > 0 && pageLimit > mTotalMovies) {
                pageLimit = mTotalMovies;
            }
            if (pageLimit > MAX_PAGE_SIZE) {
                pageLimit = (MAX_PAGE_SIZE / DEFAULT_PAGE_SIZE) * DEFAULT_PAGE_SIZE;
                Toast.makeText(mHost, String.format("Refresh Exceeds Maximum Page Size. Fetching %1$d %2$s", pageLimit, getActivity().getResources().getQuantityString(R.plurals.movies, pageLimit)), Toast.LENGTH_SHORT).show();
                mEndOfList = false;
            }
            mMovies.clear();
            mMovieAdapter = null;
            mPageNumber = 1;
        }
        fetchMoreMovies(pageLimit);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onMovieSelected((Movie) parent.getItemAtPosition(position));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean shouldFetchMoreMovies = firstVisibleItem + visibleItemCount >= totalItemCount && visibleItemCount > 0; /* visibleItemCount == 0 when the device is rotated and the view is recreated. */
        //Log.d(TAG, String.format("%1$d + %2$d >= %3$d ? %4$s", firstVisibleItem, visibleItemCount, totalItemCount, String.valueOf(shouldFetchMoreMovies)));
        if (shouldFetchMoreMovies && /* Already fetching movies? */ mProgressBar.getVisibility() != View.VISIBLE && /* End of our result list? */ totalItemCount < mTotalMovies) {
            //Log.d(TAG, String.format("%1$d < %2$d", totalItemCount, mTotalMovies));
            fetchMoreMovies(DEFAULT_PAGE_SIZE);
        }
    }

    private void fetchMoreMovies(int pageLimit) {
        recordScrollPosition();
        mProgressBar.setVisibility(View.VISIBLE);
        EventBus.post(new GetMoviesEvent(R.id.api_call_get_movies, mPageNumber, pageLimit));
    }

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
            mMovies.addAll(movies.getMovies());
            mTotalMovies = movies.getTotal();
            int movieCount = movies.getMovies().size();
            if (movieCount > DEFAULT_PAGE_SIZE) {
                mPageNumber = movieCount / DEFAULT_PAGE_SIZE;
            }
            // Only increment our page number if we received a movies result
            mPageNumber++;
        }
        if (mMovieAdapter == null) {
            mMovieAdapter = new MovieAdapter(mHost, R.layout.listview_movie_row, mMovies);
            mMovieListView.setAdapter(mMovieAdapter);
        } else if (movies != null) {
            mMovieAdapter.notifyDataSetChanged();
        }
        mMovieListView.setSelectionFromTop(mScrollPosition, mScrollOffset);
        if (mPageNumber * DEFAULT_PAGE_SIZE >= mTotalMovies) {
            mEndOfList = true;
        }
        setFooterText();
    }

    private void setFooterText() {
        if (mEndOfList) {
            mFooterTextView.setText(getString(R.string.footer_end_of_list));
        } else {
            mFooterTextView.setText(getString(R.string.footer_fetching_more));
        }
    }

    /**
     * Record where we're at in the list so we can put the user back in the same spot when they
     * return to the list.
     */
    private void recordScrollPosition() {
        /**
         * In addition to grabbing the top visible item also store the view offset
         * so we can truly keep our view exactly where it was.
         */
        mScrollPosition = mMovieListView.getFirstVisiblePosition();
        View view = mMovieListView.getChildAt(0);
        mScrollOffset = (view == null) ? 0 : view.getTop();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

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
                mEmptyTextView.setText(String.format("URL: '%1$s'\nHTTP Status Code: '%2$d'\nError: '%3$s'", error.getError().getUrl(), error.getHttpStatusCode(), error.getError().getMessage()));
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

    @DebugLog
    public interface OnFragmentInteractionListener {
        public void onMovieSelected(Movie movie);
    }

}
