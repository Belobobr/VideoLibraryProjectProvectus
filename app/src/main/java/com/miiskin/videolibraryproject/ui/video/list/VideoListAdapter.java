package com.miiskin.videolibraryproject.ui.video.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.miiskin.videolibraryproject.R;
import com.miiskin.videolibraryproject.content.data.VideoInfo;
import com.miiskin.videolibraryproject.ui.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Newshka on 08.09.2015.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    List<VideoInfo> mItems;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public VideoListAdapter(OnItemClickListener onItemClickListener) {
        mItems = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        return new ViewHolder(inflate(R.layout.layout_video_info_list_item, viewGroup), mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        VideoInfo item = getItem(position);
        viewHolder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<VideoInfo> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public VideoInfo getItem(int position) {
        return mItems.get(position);
    }

    public View inflate(int layoutRes, ViewGroup parent) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        return mLayoutInflater.inflate(layoutRes, parent, false);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnItemClickListener mOnItemClickListener;
        int mPosition;

        @Bind(R.id.title)
        TextView mTitleTextView;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(VideoInfo videoInfo, int position) {
            mPosition = position;
            mTitleTextView.setText(videoInfo.getTitle());
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClickListener(mPosition);
        }
    }
}
