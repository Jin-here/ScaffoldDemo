package com.vgaw.scaffold.view.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.viewpager.widget.ViewPager;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.view.checkable.CheckableAdapter;
import com.vgaw.scaffold.view.checkable.CheckableLayout;
import com.vgaw.scaffold.view.msgtip.MsgTipItemInterface;
import com.vgaw.scaffold.view.tab.indicator.BaseIndicator;

public class SlidingTabLayout extends CheckableLayout {
    private int mBottomBorderThickness;
    private Paint mBottomBorderPaint;
    private int mBottomLineColor = -1;

    private BaseIndicator mIndicator;

    private Paint mDividerPaint;
    private float mDividerHeight;
    private int[] mDividerColors;

    private int mSelectedPosition;
    private float mSelectionOffset;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

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
        View child = getChildAt(index);
        if (child instanceof MsgTipItemInterface) {
            ((MsgTipItemInterface) child).onMsgNumChanged(unReadCount);
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
        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());

            check(initIndex);
        }
    }

    @Override
    public void setAdapter(CheckableAdapter adapter) {
        super.setAdapter(adapter);

        validDraw();
    }

    public void setSelectedIndicator(BaseIndicator indicator) {
        mIndicator = indicator;
        mIndicator.init(getChildCount());

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
            check(position);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }
    }

    private void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;

        invalidate();
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);
            boolean equalization = array.getBoolean(R.styleable.SlidingTabLayout_tabEqualization, false);
            int dividerSize = array.getDimensionPixelSize(R.styleable.SlidingTabLayout_tabDividerSize, 0);
            array.recycle();

            setEqualization(equalization);
            setDividerSize(dividerSize);
        }

        setOnItemCheckedListener(index -> {
            if (mSelectedPosition != index) {
                mViewPager.setCurrentItem(index);
            }
        });
    }
}