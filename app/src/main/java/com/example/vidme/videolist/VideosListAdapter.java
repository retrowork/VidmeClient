package com.example.vidme.videolist;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.vidme.R;
import com.example.vidme.model.Video;

import java.util.ArrayList;
import java.util.List;

class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.ViewHolder>  {

    public interface OnItemClickListener {
        void onItemClick(Video item);
    }

    private List<Video> mVideosList;

    private final OnItemClickListener listener;

    private Context mContext;

    VideosListAdapter(Context context, OnItemClickListener listener) {
        this.mVideosList = new ArrayList<>();
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new ViewHolder(v);
    }

    public void addAll(List<Video> videos) {
        this.mVideosList.addAll(videos);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Video videoItem = mVideosList.get(position);
        holder.title.setText(videoItem.title);
        int score = videoItem.score;
        String scoreText = mContext.getResources().getQuantityString(R.plurals.numberOfLikes, score, score);
        holder.score.setText(scoreText);
        Glide.with(mContext)
                .asBitmap()
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                .load(videoItem.thumbnailUrl)
                .into(holder.videoThumbnail);
        holder.setOnClickListener(videoItem, listener);
    }

    public void clearAdapter() {
        mVideosList.clear();
    }

    @Override
    public int getItemCount() {
        return mVideosList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;

        TextView title;

        TextView score;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.video_title);
            score = (TextView) itemView.findViewById(R.id.video_score);
            videoThumbnail = (ImageView) itemView.findViewById(R.id.video_thumbnail);
        }

        public void setOnClickListener(final Video videoItem, final OnItemClickListener listener) {
            videoThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(videoItem);
                }
            });
        }
    }
}
