package com.example.vidme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.ViewHolder>  {

    public interface OnItemClickListener {
        void onItemClick(Video item);
    }

    private List<Video> mVideosList;

    private final OnItemClickListener listener;

    private Context mContext;

    VideosListAdapter(List<Video> videos, Context context, OnItemClickListener listener) {
        this.mVideosList = videos;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Video videoItem = mVideosList.get(position);
        holder.title.setText(videoItem.title);
        int score = videoItem.score;
        String scoreText = mContext.getResources().getQuantityString(R.plurals.numberOfLikes, score, score);
        holder.score.setText(scoreText);
        Glide.with(mContext)
                .load(videoItem.thumbnailUrl)
                .into(holder.videoThumbnail);
        holder.videoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(videoItem);
            }
        });
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
    }
}
