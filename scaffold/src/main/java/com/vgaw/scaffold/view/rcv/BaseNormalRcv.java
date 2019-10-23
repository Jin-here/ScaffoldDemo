package com.vgaw.scaffold.view.rcv;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BaseNormalRcv<T> extends RecyclerView {
    protected EasyRcvAdapter<T> mAdapter;
    protected List<T> mDataList = new ArrayList<>();

    private boolean mScrollable;

    public BaseNormalRcv(Context context) {
        super(context);
        init();
    }

    public BaseNormalRcv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseNormalRcv(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mScrollable) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST));
        }
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
}
