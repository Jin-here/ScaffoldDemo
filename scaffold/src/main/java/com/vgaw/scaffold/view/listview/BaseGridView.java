package com.vgaw.scaffold.view.listview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.GridView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class BaseGridView<T> extends GridView {
    protected EasyAdapter<T> mAdapter;
    protected List<T> mDataList = new ArrayList<>();

    private boolean mScrollable;

    public BaseGridView(Context context) {
        super(context);
        init();
    }

    public BaseGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    public void updateData(List<T> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        mAdapter.notifyDataSetChanged();
    }

    public void setScrollable(boolean scrollable) {
        mScrollable = scrollable;
    }

    protected void init() {
        setScrollable(true);
    }
}
