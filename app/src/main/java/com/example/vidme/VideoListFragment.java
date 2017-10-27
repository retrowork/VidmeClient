package com.example.vidme;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.vidme.MainActivity.FEATURED;
import static com.example.vidme.MainActivity.LIST_TYPE;


public class VideoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = this.getClass().getSimpleName();

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view =  inflater.inflate(R.layout.featured_fragment, container, false);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.featured_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.featured_recyclerview);
        mRecyclerView.setLayoutManager(llm);

        Bundle args = getArguments();
        ArrayList<Video> videos = new ArrayList<>();
        if (args.getInt(LIST_TYPE) == FEATURED) {
            videos.add(new Video() {
                {
                    title = "New video";
                    thumbnailUrl = "https://d1wst0behutosd.cloudfront.net/videos/18524931/thumb.jpg?v2r1508952664";
                    score = 112;
                    completeUrl = "https://api.vid.me/video/18544461/stream?format=hls";
                }
            });
            videos.add(new Video() {
                {
                    title = "New video 2";
                    thumbnailUrl = "https://d1wst0behutosd.cloudfront.net/videos/18503481/thumb.jpg?v2r1508872155";
                    score = 1;
                    completeUrl = "https://vid.me/e/gjMy";
                }
            });
        }
        VideosListAdapter adapter = new VideosListAdapter(videos, getActivity(), new VideosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("VIDEO_URL", item.completeUrl);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onRefresh() {
        Log.v(TAG, "onRefresh");
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
