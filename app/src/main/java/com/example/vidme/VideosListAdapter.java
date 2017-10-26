package com.example.vidme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.ViewHolder> {

    private List<Video> mVideosList;

    private Context mContext;

    VideosListAdapter(List<Video> videos, Context context) {
        this.mVideosList = videos;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mVideosList.get(position).title);
        int score = mVideosList.get(position).score;
        String scoreText = mContext.getResources().getQuantityString(R.plurals.numberOfLikes, score, score);
        holder.score.setText(scoreText);
        Glide.with(mContext)
                .load(mVideosList.get(position).thumbnailUrl)
                .into(holder.videoThumbnail);
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
