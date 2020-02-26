package com.vgaw.scaffolddemo.page.demo.compoment.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.viewpager.widget.PagerAdapter;

import com.vgaw.scaffold.util.phone.DensityUtil;
import com.vgaw.scaffold.view.vp.UltraViewPager;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.data.MockUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AdViewPager extends UltraViewPager {
    protected List<String> mDataList = new ArrayList<>();

    public AdViewPager(Context context) {
        super(context);
        init();
    }

    public AdViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        //UltraPagerAdapter 绑定子view到UltraViewPager
        PagerAdapter adapter = new ADPagerAdapter(getContext(), mDataList);
        setAdapter(adapter);

        //内置indicator初始化
        initIndicator();
        //设置indicator样式
        int padding = DensityUtil.dp2px(getContext(), 8);
        int margin = DensityUtil.dp2px(getContext(), 12);
        getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(getResources().getColor(R.color.colorAccent))
                .setNormalColor(getResources().getColor(android.R.color.darker_gray))
                .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()))
                .setIndicatorPadding(padding)
                .setMargin(0, 0, margin, margin);
        //设置indicator对齐方式
        getIndicator().setGravity(Gravity.END | Gravity.BOTTOM);
        //构造indicator,绑定到UltraViewPager
        getIndicator().build();

        //设定页面循环播放
        setInfiniteLoop(true);
        //设定页面自动切换  间隔5秒
        setAutoScroll(5000);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        fetchData();
    }

    private void fetchData() {
        MockUtil.buildADList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> updateData(strings));
    }

    private void updateData(List<String> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);

        // 注意：此处不能用自己的adapter
        getViewPager().getAdapter().notifyDataSetChanged();
    }
}
