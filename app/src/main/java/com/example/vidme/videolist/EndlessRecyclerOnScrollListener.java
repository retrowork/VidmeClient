package com.example.vidme.videolist;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    /**
     * The total number of items in the dataset after the last load
     */
    private int mPreviousTotal = 0;
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean mLoading = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        String TAG = getClass().getSimpleName();
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getAdapter().getItemCount();
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
            }
        }
        int visibleThreshold = 5;
  //      Log.v(TAG, "totalItemCount = " + totalItemCount + " firstVisibleItem = " + firstVisibleItem + " visibleItemCount= " + visibleItemCount);
        if (!mLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold) && dy > 0) {
            // End has been reached

            onLoadMore();

            mLoading = true;
        }
    }

    public void reset(int previousTotal, boolean loading) {
        this.mPreviousTotal = previousTotal;
        this.mLoading = loading;
    }

    public abstract void onLoadMore();
}
