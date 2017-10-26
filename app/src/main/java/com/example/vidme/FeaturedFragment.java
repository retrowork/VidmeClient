package com.example.vidme;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FeaturedFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.featured_fragment, container, false);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.featured_recyclerview);
        mRecyclerView.setLayoutManager(llm);

        ArrayList<Video> videos = new ArrayList<>();
        videos.add(new Video() {
            {
                title = "New video";
                thumbnailUrl = "https://d1wst0behutosd.cloudfront.net/videos/18524931/thumb.jpg?v2r1508952664";
                score = 112;
                url = "https://vid.me/QZn7M";
            }
        });
        videos.add(new Video() {
            {
                title = "New video 2";
                thumbnailUrl = "https://d1wst0behutosd.cloudfront.net/videos/18503481/thumb.jpg?v2r1508872155";
                score = 1;
                url = "https:\\/\\/vid.me\\/QZn7M";
            }
        });
        VideosListAdapter adapter = new VideosListAdapter(videos, getActivity());
        mRecyclerView.setAdapter(adapter);
        return view;
    }
}
