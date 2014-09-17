package com.androidfu.foundation.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidfu.foundation.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by billmote on 9/17/14.
 */
public class ExampleObjectAdapter extends ArrayAdapter<Object> {

    Context mContext;
    List<Object> mObjects;

    public ExampleObjectAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mObjects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Object object = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.listview_simple_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mLvRow.setBackgroundColor(position % 2 == 0 ? 0xFFCCCCCC : 0xFFEEEEEE);
        holder.mTvOne.setText(object.toString());
        holder.mTvTwo.setText(position);

        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'listview_simple_row.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.tv_one)
        TextView mTvOne;
        @InjectView(R.id.tv_two)
        TextView mTvTwo;
        @InjectView(R.id.lv_row)
        LinearLayout mLvRow;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
