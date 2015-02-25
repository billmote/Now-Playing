package com.androidfu.nowplaying.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidfu.nowplaying.app.BuildConfig;
import com.androidfu.nowplaying.app.R;
import com.androidfu.nowplaying.app.model.movies.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by billmote on 9/17/14.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, int resource, List<Movie> movies) {
        super(context, resource, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.listview_movie_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mLvRow.setBackgroundColor(position % 2 == 0 ? 0xFFCCCCCC : 0xFFEEEEEE);
        holder.mTvTitle.setText(movie.getTitle());
        holder.mTvUserRating.setRating((float) movie.getRatings().getAudienceScore() / 20);
        holder.mTvMpaaRating.setText(movie.getMpaaRating());
        if (!BuildConfig.DEBUG) {
            /*
                If we're releasing to Google Play then replace the movie posters with a static icon
                as Google does not feel that I have the rights to display the thumbnails returned to
                me in the Rotten Tomatoes API.  I don't know if they're right or not, but it's not
                worth fighting about over a freely available sample app ;)
                
                <Insert Frozen's "Let It Go" here.>
             */
            holder.mIvThumbnail.setImageDrawable(getContext().getResources().getDrawable(R.drawable.movie_reel_thumb));
        } else {
            /*
                If you're building this application for debug purposes then display the movie posters
                returned from Rotten Tomatoes 'in_theaters.json' endpoint.
             */
            Picasso.with(getContext())
                    .load(movie.getPosters().getThumbnail())
                    .placeholder(R.drawable.admit_one)
                    .error(R.drawable.emoticon_sad)
                    .into(holder.mIvThumbnail, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {
                                }
                            }
                    );
        }
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'listview_movie_row.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.iv_thumbnail)
        ImageView mIvThumbnail;
        @InjectView(R.id.tv_title)
        TextView mTvTitle;
        @InjectView(R.id.tv_user_rating)
        RatingBar mTvUserRating;
        @InjectView(R.id.tv_mpaa_rating)
        TextView mTvMpaaRating;
        @InjectView(R.id.lv_row)
        LinearLayout mLvRow;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
