package com.androidfu.foundation.handlers;

import android.widget.ImageView;

import com.androidfu.foundation.BuildConfig;
import com.androidfu.foundation.model.SomePojo;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.Log;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/1/14.
 */
public class GetNetworkImageRequestHandler {

    private static final String TAG = GetNetworkImageRequestHandler.class.getSimpleName();

    @DebugLog
    public GetNetworkImageRequestHandler() {

    }

    @DebugLog
    @Subscribe
    public void handleNetworkImageRequest(GetNetworkImageRequest request) {

        ImageView imageView = request.imageView;
        String url = request.url;

        if (BuildConfig.DEBUG) {
            Picasso.with(imageView.getContext()).setIndicatorsEnabled(true);
            Picasso.with(imageView.getContext()).setLoggingEnabled(true);
        }
        Picasso.with(imageView.getContext())
                .load(url)
                .fit()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.w(TAG, "Got our image.");
                        EventBus.post(new SomePojo("SUCCESS"));
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
