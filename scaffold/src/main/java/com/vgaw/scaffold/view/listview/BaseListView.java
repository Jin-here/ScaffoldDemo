package com.vgaw.scaffold.view.listview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class BaseListView<T> extends LoadMoreListView {
    protected EasyAdapter<T> mAdapter;
    protected List<T> mDataList = new ArrayList<>();

    private boolean mScrollable;

    public BaseListView(Context context) {
        super(context);
        init();
    }

    public BaseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    public void updateData(List<T> dataList) {
        mDataList.clear();
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
