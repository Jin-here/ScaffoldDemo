package com.vgaw.scaffold.view.rcv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.vgaw.scaffold.view.rcv.decoration.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRcv<T> extends SmartRefreshLayout {
    protected static final int PAGE_SIZE = 50;
    private RecyclerView mContentView;

    private List<T> mDataList = new ArrayList<>();
    private EasyRcvAdapter<T> mAdapter;

    private int mCrtPage;

    public BaseRcv(Context context) {
        super(context);
        init();
    }

    public BaseRcv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnRefreshListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                fetchData(true);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                resetPage();
                fetchData(false);
            }
        });

        autoRefresh(300);
    }

    protected abstract void fetchData(boolean loadMore);

    protected abstract EasyRcvHolder<T> getItemHolder(int type);

    protected int getColCount() {
        return 1;
    }

    protected int getItemPadding() {
        return 8;
    }

    protected void notifyFetchDataSuc(boolean loadMore, List<T> dataList) {
        if (!loadMore) {
            mDataList.clear();
        }
        mCrtPage++;
        mDataList.addAll(dataList);
        mAdapter.notifyDataSetChanged();
        boolean noMoreData = (dataList.size() < PAGE_SIZE);
        if (loadMore) {
            finishLoadMore();
        } else {
            finishRefresh();
        }
        setNoMoreData(noMoreData);
    }

    protected void notifyFetchDataFail(boolean loadMore) {
        if (loadMore) {
            finishLoadMore();
        } else {
            finishRefresh();
        }
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new GridDividerItemDecoration(getContext(), getItemPadding());
    }

    private void resetPage() {
        mCrtPage = 0;
    }

    private void init() {
        mContentView = new RecyclerView(getContext());
        addView(mContentView);

        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), getColCount());
        mContentView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration gridDividerItemDecoration = getItemDecoration();
        mContentView.addItemDecoration(gridDividerItemDecoration);

        mAdapter = new EasyRcvAdapter<T>(getContext(), mDataList) {
            @Override
            protected EasyRcvHolder getHolder(@NonNull ViewGroup parent, int type) {
                return getItemHolder(type);
            }
        };
        mContentView.setAdapter(mAdapter);
    }
}