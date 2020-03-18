/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vgaw.scaffold.view.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.view.tab.indicator.BaseIndicator;

import timber.log.Timber;

public class SlidingTabLayout extends RadioGroup {
    private int mBottomBorderThickness;
    private Paint mBottomBorderPaint;
    private int mBottomLineColor = -1;

    private BaseIndicator mIndicator;

    private Paint mDividerPaint;
    private float mDividerHeight;
    private int[] mDividerColors;

    private int mTabViewLayoutId;
    private Object mTabViewTextViewTag;

    private int mSelectedPosition;
    private float mSelectionOffset;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private boolean mEqualization;
    private int mPadding;

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);
            mEqualization = array.getBoolean(R.styleable.SlidingTabLayout_tabEqualization, false);
            mPadding = array.getDimensionPixelSize(R.styleable.SlidingTabLayout_tabPadding, 0);
            array.recycle();
        }
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link SlidingTabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    public void setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, 0);
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager, int initIndex) {
        removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();

            checkIndex(initIndex);
        }
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewTag tag of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(@LayoutRes int layoutResId, Object textViewTag) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewTag = textViewTag;
    }

    public void setSelectedIndicator(BaseIndicator indicator) {
        mIndicator = indicator;

        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), mBottomBorderThickness);

        validDraw();
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setDivider(int thickness, int heightPercent, int... colors) {
        mDividerHeight = heightPercent;
        mDividerColors = colors;

        mDividerPaint = new Paint();
        mDividerPaint.setStrokeWidth(thickness);

        validDraw();
    }

    public void setBottomLineColor(int thickness, @ColorRes int color) {
        mBottomLineColor = color;
        mBottomBorderThickness = thickness;

        mBottomBorderPaint = new Paint();
        mBottomBorderPaint.setColor(mBottomLineColor);

        validDraw();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();
        final int childCount = getChildCount();
        final int dividerHeightPx = (int) (Math.min(Math.max(0f, mDividerHeight), 1f) * height);

        // Thick colored underline below the current selection
        if (mIndicator != null) {
            mIndicator.onDraw(canvas, this, mSelectedPosition, mSelectionOffset);
        }

        // Thin underline along the entire bottom edge
        boolean showBottomLine = (mBottomLineColor != -1);
        if (showBottomLine) {
            canvas.drawRect(0, height - mBottomBorderThickness, getWidth(), height, mBottomBorderPaint);
        }

        // Vertical separators between the titles
        boolean showDivider = (mDividerColors != null);
        if (showDivider) {
            int separatorTop = (height - dividerHeightPx) / 2;
            for (int i = 0; i < childCount - 1; i++) {
                View child = getChildAt(i);
                mDividerPaint.setColor(mDividerColors[i]);
                canvas.drawLine(child.getRight(), separatorTop, child.getRight(),
                        separatorTop + dividerHeightPx, mDividerPaint);
            }
        }
    }

    private void validDraw() {
        if (willNotDraw()) {
            setWillNotDraw(false);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            checkIndex(position);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }
    }

    private void checkIndex(int index) {
        RadioButton child = (RadioButton) getChildAt(index);
        if (!child.isChecked()) {
            check(child.getId());
        }
    }

    private void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;

        invalidate();
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final TabClickListener tabClickListener = new TabClickListener();

        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View tabView = null;
            RadioButton tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, this,
                        false);
                if (tabView instanceof RadioButton) {
                    tabTitleView = (RadioButton) tabView;
                } else {
                    tabTitleView = tabView.findViewWithTag(mTabViewTextViewTag);
                }
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabTitleView.setOnCheckedChangeListener(tabClickListener);

            LayoutParams params = null;
            if (mEqualization) {
                params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
            } else {
                params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }

            if (mPadding != 0) {
                if (i < count - 1) {
                    params.rightMargin = mPadding;
                }
            }
            addView(tabView, params);
        }
    }

    private class TabClickListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for (int i = 0; i < getChildCount(); i++) {
                    if (buttonView == getChildAt(i)) {
                        Timber.d("onChecked: %d, %d, %d", buttonView.getId(), mViewPager.getCurrentItem(), i);
                        if (mSelectedPosition != i) {
                            mViewPager.setCurrentItem(i);
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, Object)}}.
     */
    protected TextView createDefaultTabView(Context context) {
        RadioButton textView = new RadioButton(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setButtonDrawable(null);
        textView.setTextColor(getResources().getColor(R.color.tab_text_color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }

        return textView;
    }
}