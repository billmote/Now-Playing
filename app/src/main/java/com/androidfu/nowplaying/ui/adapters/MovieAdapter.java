package com.androidfu.nowplaying.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidfu.nowplaying.R;
import com.androidfu.nowplaying.model.movies.Movie;
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
