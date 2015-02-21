package com.androidfu.foundation.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidfu.foundation.R;
import com.androidfu.foundation.model.movies.Movie;
import com.androidfu.foundation.ui.fragments.MovieListFragment;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.GoogleAnalyticsHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;
import hugo.weaving.DebugLog;


@DebugLog
public class MainActivity extends BaseActivity implements MovieListFragment.OnFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        Tracker tracker = GoogleAnalyticsHelper.getInstance().getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        tracker.send(new HitBuilders.AppViewBuilder().build());

        MovieListFragment movieListFragment = (MovieListFragment) getFragmentManager().findFragmentByTag(MovieListFragment.TAG);
        if (movieListFragment == null) {
            movieListFragment = MovieListFragment.newInstance();
        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    //.addToBackStack(PlaceholderFragment.TAG)
                    //.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_left, R.anim.slide_in_from_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, movieListFragment, MovieListFragment.TAG)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        Toast.makeText(this, String.format("You chose: %1$s", movie.getTitle()), Toast.LENGTH_SHORT).show();
    }
}
