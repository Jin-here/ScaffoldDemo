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
import com.vgaw.scaffold.ScaffoldSetting;
import com.vgaw.scaffold.view.rcv.decoration.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRcv<T> extends SmartRefreshLayout {
    protected RecyclerView mContentView;

    protected List<T> mDataList = new ArrayList<>();
    protected EasyRcvAdapter<T> mAdapter;

    protected int mCrtPage;
    protected boolean mLoadMore;

    private OnItemClickListener<T> mItemClickListener;

    public void setItemClickListener(OnItemClickListener<T> listener) {
        mItemClickListener = listener;
    }

    public BaseRcv(Context context) {
        super(context);
        init();
    }

    public BaseRcv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void refreshSilently() {
        if (mListener != null) {
            resetPage();
            mListener.fetchData(mCrtPage);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnRefreshListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mLoadMore = true;
                fetchData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mLoadMore = false;
                resetPage();
                fetchData();
            }
        });

        if (startWithLoad()) {
            autoRefresh(300);
        }
    }

    protected boolean startWithLoad() {
        return true;
    }

    protected void fetchData() {
        if (mListener != null) {
            mListener.fetchData(mCrtPage);
        }
    }

    protected abstract EasyRcvHolder<T> getItemHolder(@NonNull ViewGroup parent, int type);

    protected int getItemType(int position, T item) {
        return 0;
    }

    protected int getColCount() {
        return 1;
    }

    protected int getItemPadding() {
        return 8;
    }

    protected void filterList() {}

    public void updateWithoutRefresh(List<T> list) {
        mDataList.clear();
        mDataList.addAll(list);

        mAdapter.notifyDataSetChanged();
    }
    public void updateRefresh() {
        mAdapter.notifyDataSetChanged();
    }
    public void notifyFetchDataSuc(List<T> dataList) {
        if(dataList!= null){
            if (!mLoadMore) {
                mDataList.clear();
            }
            mCrtPage++;
            mDataList.addAll(dataList);
            filterList();
            mAdapter.notifyDataSetChanged();
            boolean noMoreData = (dataList.size() < ScaffoldSetting.PAGE_SIZE);
            if (mLoadMore) {
                finishLoadMore();
            } else {
                finishRefresh();
            }
            setNoMoreData(noMoreData);
        }

    }

    public void notifyFetchDataFail() {
        if (mLoadMore) {
            finishLoadMore();
        } else {
            finishRefresh();
        }
    }

    protected void callItemClick(T item, int position) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClicked(item, position);
        }
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new GridDividerItemDecoration(getContext(), getItemPadding());
    }

    protected void resetPage() {
        mCrtPage = 0;
    }

    private void init() {
        mContentView = new RecyclerView(getContext());
        addView(mContentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), getColCount());
        mContentView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration gridDividerItemDecoration = getItemDecoration();
        mContentView.addItemDecoration(gridDividerItemDecoration);

        mAdapter = new EasyRcvAdapter<T>(getContext(), mDataList) {
            @Override
            public int getItemViewType(int position) {
                return getItemType(position, mDataList.get(position));
            }

            @Override
            protected EasyRcvHolder getHolder(@NonNull ViewGroup parent, int type) {
                return getItemHolder(parent, type);
            }
        };
        mContentView.setAdapter(mAdapter);
    }

    private FetchDataListener mListener;

    public void setFetchDataListener(FetchDataListener listener) {
        mListener = listener;
    }

    public interface FetchDataListener {
        void fetchData(int crtPage);
    }
}