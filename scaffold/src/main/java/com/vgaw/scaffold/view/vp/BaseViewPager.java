package com.vgaw.scaffold.view.vp;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

public class BaseViewPager<T> extends UltraViewPager {
    protected EasyPagerAdapter mAdapter;
    protected List<T> mDataList = new ArrayList<>();

    public BaseViewPager(Context context) {
        super(context);
        init();
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {}

    public void addData(List<T> dataList) {
        mDataList.addAll(dataList);

        // 注意：此处不能用自己的adapter
        getViewPager().getAdapter().notifyDataSetChanged();
    }
}
