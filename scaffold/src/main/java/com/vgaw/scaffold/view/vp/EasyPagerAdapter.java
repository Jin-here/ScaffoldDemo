package com.vgaw.scaffold.view.vp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public abstract class EasyPagerAdapter<T> extends PagerAdapter {
    protected Context mContext;
    private List<T> mDataList;

    public EasyPagerAdapter(Context context, List<T> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return (mDataList == null ? 0 : mDataList.size());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = instantiateView(position);
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    protected T getItem(int position) {
        if (mDataList != null && mDataList.size() > position) {
            return mDataList.get(position);
        }
        return null;
    }

    protected abstract View instantiateView(int position);
}
