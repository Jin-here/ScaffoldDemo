package com.vgaw.scaffold.view.rcv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class BaseRcv<T> extends XRecyclerView {
    protected EasyRcvAdapter<T> mAdapter;
    protected List<T> mDataList = new ArrayList<>();

    protected static final int PAGE_SIZE = 20;
    protected int mOffset;

    private boolean mScrollable;

    public BaseRcv(Context context) {
        super(context);
        init();
    }

    public BaseRcv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseRcv(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mScrollable) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    View.MeasureSpec.AT_MOST));
        }
    }

    public void optimize(boolean fixSize) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        setHasFixedSize(fixSize);
    }

    public void clearData() {
        mDataList.clear();

        mAdapter.notifyDataSetChanged();
    }

    public void updateData(List<T> dataList) {
        mDataList.clear();
        addData(dataList);
    }

    public void addData(T item) {
        mDataList.add(item);

        mAdapter.notifyDataSetChanged();
    }

    public void addData(List<T> dataList) {
        mDataList.addAll(dataList);

        mAdapter.notifyDataSetChanged();
    }

    protected void init() {
        setScrollable(true);
    }

    protected void setScrollable(boolean scrollable) {
        mScrollable = scrollable;
    }

    /**
     * 成功获取一页数据后调用
     */
    protected void onOnePageAchieved() {
        mOffset += PAGE_SIZE;
    }

    protected void resetPage() {
        mOffset = 0;
    }
}
