package com.vgaw.scaffold.view.vp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NoScrollViewPager extends ViewPager {
    private boolean mScrollDisabled;

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScrollDisabled) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void enableScroll(boolean enabled) {
        mScrollDisabled = !enabled;
    }
}
