package com.example.vidme;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.concurrent.ExecutionException;

public class PlayerActivity extends AppCompatActivity implements ExoPlayer.EventListener{

    private SimpleExoPlayerView mPlayerView;

    private SimpleExoPlayer mExoPlayer;

    private ProgressBar mProgressBar;

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        mProgressBar = (ProgressBar) findViewById(R.id.video_progress_bar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = getIntent().getStringExtra("VIDEO_URL");
        Uri uri = Uri.parse(url);
        initializePlayer(uri);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            mExoPlayer.addListener(this);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(this, "Vidme Client");
            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(this, userAgent);
            MediaSource mediaSource = new HlsMediaSource(mediaUri,
                    dataSourceFactory, null, null);
            mExoPlayer.prepare(mediaSource);
            mPlayerView.setControllerHideOnTouch(true);
            mPlayerView.setBackgroundColor(getResources().getColor(R.color.black));
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_BUFFERING) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }
}
