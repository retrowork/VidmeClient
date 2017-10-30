package com.example.vidme.videolist;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.vidme.player.PlayerActivity;
import com.example.vidme.R;
import com.example.vidme.model.Response;
import com.example.vidme.model.Video;
import com.example.vidme.network.VidmeService;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.vidme.navigation.MainActivity.LIST_TYPE;


public class VideoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = this.getClass().getSimpleName();

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private VidmeService mVidmeService;

    private VideosListAdapter mAdapter;

    private final int LIMIT = 10; // limit per page

    private int mOffset;

    private int mListType;

    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.featured_fragment, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mVidmeService = new VidmeService();
        mOffset = 0;

        mProgressBar = (ProgressBar) view.findViewById(R.id.item_progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.featured_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.featured_recyclerview);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        EndlessRecyclerOnScrollListener listener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                VideoListFragment.this.onLoadMore();
            }
        };
        mRecyclerView.addOnScrollListener(listener);
        Bundle args = getArguments();
        mListType = args.getInt(LIST_TYPE);
        mAdapter = new VideosListAdapter(getActivity(), new VideosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                Log.v(TAG, item.thumbnailUrl);
                intent.putExtra("VIDEO_URL", item.completeUrl);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        receiveData(mListType);
        return view;
    }

    private void receiveData(int listType) {
        try {
            Observer<Response> myObserver = new Observer<Response>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Response value) {
                    List<Video> videos = value.getVideos();
                    if (mOffset == 0) {
                        mAdapter.clearAdapter();
                    }
                    mAdapter.addAll(videos);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    Log.v(TAG, "onError " + e.getMessage());
                }

                @Override
                public void onComplete() {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            };

            String token = getToken();
            mVidmeService.getVideos(listType, LIMIT, mOffset, token)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(myObserver);

        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private String getToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sp.getString("ACCESS_TOKEN", "");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void onRefresh() {
        receiveData(mListType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.clearOnScrollListeners();
    }

    public void onLoadMore() {
        mOffset += 10;
        mProgressBar.setVisibility(View.VISIBLE);
        Log.v(TAG, "OFFSET : " + mOffset);
        receiveData(mListType);
    }
}