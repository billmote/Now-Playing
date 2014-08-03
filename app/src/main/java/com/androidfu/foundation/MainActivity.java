package com.androidfu.foundation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.androidfu.foundation.handlers.GetApplicationSettingsRequest;
import com.androidfu.foundation.handlers.GetNetworkImageRequest;
import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.model.SomePojo;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.Log;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;


public class MainActivity extends BaseActivity {

    // Every class gets a public TAG
    public static final String TAG = MainActivity.class.getSimpleName();

    // Example of our naming convention KEY_BUNDLE..., KEY_PREFS..., etc.
    public static final String KEY_BUNDLE_SOME_PARCELABLE_POJO = "our_parcelable_pojo";

    // Our ButterKnife view injections
    @InjectView(R.id.container)
    FrameLayout mContainer;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register this with ButterKnife
        ButterKnife.inject(this);

        // Register our Activity with the Otto Bus singleton
        EventBus.register(this);

        // Example of how we're going to use resources to handle screen size and orientation
        Log.i(TAG, String.format("We're on a large screen? %1$s", getResources().getBoolean(R.bool.large_display)));

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
        EventBus.post(new GetApplicationSettingsRequest());

    }

    @DebugLog
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @DebugLog
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Our Parceler library marshals/unmarshals our data for us with wrap() and unWrap().  Parcelable is exponentially faster than serializable.
        outState.putParcelable(KEY_BUNDLE_SOME_PARCELABLE_POJO, Parcels.wrap(new SomePojo()));
    }

    @DebugLog
    @Subscribe
    // Listen for our data response.  The name of this method is meaningless, but should describe what's going on.
    public void getCurrentPojo(SomePojo pojo) {
        Log.w(TAG, String.format("Our activity was listening and got: %1$s", pojo.someField));
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public final String TAG = PlaceholderFragment.class.getSimpleName();

        @InjectView(R.id.button)
        Button mButton;
        @InjectView(R.id.imageView)
        ImageView mImageView;

        private String mUrl = "http://i2.cdn.turner.com/cnn/dam/assets/130530161523-100-beaches-crane-beach-horizontal-gallery.jpg";

        @DebugLog
        public PlaceholderFragment() {
            // Register our Fragment with the Otto Bus singleton
            EventBus.register(this);
        }

        @DebugLog
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.inject(this, rootView);
            return rootView;
        }

        @DebugLog
        @OnClick(R.id.button)
        public void fetchImage() {
            EventBus.post(new GetNetworkImageRequest(mImageView, mUrl));
        }

        @DebugLog
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.reset(this);
        }

        @DebugLog
        @Subscribe
        // Listen for our data response.  The name of this method is meaningless, but should describe what's going on.
        public void getCurrentPojo(SomePojo pojo) {
            Log.w(TAG, String.format("Our fragment was listening and got: %1$s", pojo.someField));
        }

        @DebugLog
        @Subscribe
        public void updateButtonText(ApplicationSettings applicationSettings) {
            mButton.setText(applicationSettings.someField);
        }

    }

}
