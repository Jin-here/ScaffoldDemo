package com.vgaw.scaffold.view.checkable;

import android.content.Context;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public abstract class CheckableAdapter<T> {
    protected Context mContext;

    private final List<T> mDataList;

    public CheckableAdapter(Context context, List<T> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    public CheckableAdapter(Context context, T[] dataArray) {
        mContext = context;
        mDataList = Arrays.asList(dataArray);
    }

    public abstract View createView(int position);

    public T getItem(int position) {
        return mDataList.get(position);
    }

    public int getCount() {
        return mDataList.size();
    }
}
