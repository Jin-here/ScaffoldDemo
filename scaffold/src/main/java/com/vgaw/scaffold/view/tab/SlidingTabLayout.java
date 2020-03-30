package com.vgaw.scaffold.view.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.view.checkable.CheckableAdapter;
import com.vgaw.scaffold.view.tab.indicator.BaseIndicator;

public class SlidingTabLayout extends HorizontalScrollView {
    private SlidingTabLayoutInternal mSlidingTabLayoutInternal;
    private ViewPager mViewPager;

    public SlidingTabLayout(Context context) {
        super(context);
        init(null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setMsgTip(int index, int unReadCount) {
        mSlidingTabLayoutInternal.setMsgTip(index, unReadCount);
    }

    public void setViewPager(ViewPager viewPager) {
        mSlidingTabLayoutInternal.setViewPager(viewPager, 0);
    }

    public void setViewPager(@NonNull ViewPager viewPager, int initIndex) {
        if (viewPager != null && viewPager != mViewPager) {
            if (mViewPager != null) {
                mViewPager.removeOnPageChangeListener(mPagerListener);
            }
            mSlidingTabLayoutInternal.setViewPager(viewPager, initIndex);
            mViewPager = viewPager;
            mViewPager.addOnPageChangeListener(mPagerListener);
        }
    }

    public void setAdapter(CheckableAdapter adapter) {
        mSlidingTabLayoutInternal.setAdapter(adapter);
    }

    public void setSelectedIndicator(BaseIndicator indicator) {
        mSlidingTabLayoutInternal.setSelectedIndicator(indicator);
    }

    public void setDivider(int thickness, int heightPercent, int... colors) {
        mSlidingTabLayoutInternal.setDivider(thickness, heightPercent, colors);
    }

    public void setBottomLineColor(int thickness, @ColorRes int color) {
        mSlidingTabLayoutInternal.setBottomLineColor(thickness, color);
    }

    private void init(AttributeSet attrs) {
        setHorizontalScrollBarEnabled(false);

        mSlidingTabLayoutInternal = new SlidingTabLayoutInternal(getContext());
        addView(mSlidingTabLayoutInternal, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);
            boolean equalization = array.getBoolean(R.styleable.SlidingTabLayout_tabEqualization, false);
            int dividerSize = array.getDimensionPixelSize(R.styleable.SlidingTabLayout_tabDividerSize, 0);
            array.recycle();

            mSlidingTabLayoutInternal.setEqualization(equalization);
            mSlidingTabLayoutInternal.setDividerSize(dividerSize);
        }
    }

    private ViewPager.OnPageChangeListener mPagerListener = new ViewPager.OnPageChangeListener() {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            View selectedTitle = mSlidingTabLayoutInternal.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                scrollToTab(position, 0);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }
    };

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mSlidingTabLayoutInternal.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mSlidingTabLayoutInternal.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            /*if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }*/

            scrollTo(targetScrollX, 0);
        }
    }
}
