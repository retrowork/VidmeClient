package com.example.vidme.videolist;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static com.example.vidme.navigation.MainActivity.LIST_TYPE;


public class VideoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.videolist_recyclerview)
    RecyclerView mRecyclerView;

    EndlessRecyclerOnScrollListener mScrollListener;

    @BindView(R.id.errorView)
    View mErrorView;

    @BindView(R.id.loadingView)
    View mLoadingView;

    @BindView(R.id.swipe_videoslist)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private VidmeService mVidmeService;

    private VideosListAdapter mAdapter;

    private final int LIMIT = 10; // limit per page

    private int mOffset;

    private int mListType;

    private Unbinder mUnbinder;

    @BindView(R.id.item_progress_bar)
    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.video_list_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mVidmeService = new VidmeService();
        mOffset = 0;

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                VideoListFragment.this.onLoadMore();
            }
        };

        Bundle args = getArguments();
        mListType = args.getInt(LIST_TYPE);
        mAdapter = new VideosListAdapter(getActivity(), new VideosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("VIDEO_URL", item.completeUrl);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveData(mListType, false);
            }
        });
        receiveData(mListType, false);
        mRecyclerView.addOnScrollListener(mScrollListener);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void receiveData(final int listType, final boolean isRefreshing) {
        try {
            Observer<Response> myObserver = new Observer<Response>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Response value) {
                    mErrorView.setVisibility(View.GONE);
                    mLoadingView.setVisibility(View.GONE);
                    List<Video> videos = value.getVideos();
                    if (isRefreshing) {
                        mScrollListener.reset(0, true);
                        mAdapter.clearAdapter();
                    }
                    mAdapter.addAll(videos);
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof HttpException) {
                        HttpException exception = (HttpException) e;
                    } else {
                        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (manager.getActiveNetworkInfo() == null) {
                            setErrorView();
                        }
                    }
                }

                @Override
                public void onComplete() {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            };

            String token = getToken();
            mLoadingView.setVisibility(View.VISIBLE);
            mVidmeService.getVideos(listType, LIMIT, mOffset, token)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(myObserver);

        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void setErrorView() {
        mErrorView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
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
        mScrollListener.reset(0, true);
        Log.v(TAG, "onResume");
    }

    @Override
    public void onRefresh() {
        mOffset = 0;
        Log.v(TAG, "onRefresh");
        receiveData(mListType, true);
    }

    public void onLoadMore() {
        mProgressBar.setVisibility(View.VISIBLE);
        mOffset+=10;
        receiveData(mListType, false);
    }
}
