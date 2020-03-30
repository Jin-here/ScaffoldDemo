package com.vgaw.scaffold.view.tab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.vgaw.scaffold.view.checkable.CheckableAdapter;
import com.vgaw.scaffold.view.checkable.CheckableLayout;
import com.vgaw.scaffold.view.msgtip.MsgTipItemInterface;
import com.vgaw.scaffold.view.msgtip.MsgTipLayoutInterface;
import com.vgaw.scaffold.view.tab.indicator.BaseIndicator;

// TODO: 2020/3/30 support vertical
class SlidingTabLayoutInternal extends CheckableLayout implements MsgTipLayoutInterface {
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
    private ViewPager.OnPageChangeListener mPagerListener = new InternalViewPagerListener();

    public SlidingTabLayoutInternal(Context context) {
        super(context);
        init(null);
    }

    public SlidingTabLayoutInternal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SlidingTabLayoutInternal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public void setMsgTip(int index, int unReadCount) {
        View child = getChildAt(index);
        if (child instanceof MsgTipItemInterface) {
            ((MsgTipItemInterface) child).onMsgNumChanged(unReadCount);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, 0);
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(@NonNull ViewPager viewPager, int initIndex) {
        if (viewPager != null && viewPager != mViewPager) {
            if (mViewPager != null) {
                mViewPager.removeOnPageChangeListener(mPagerListener);
            }
            mViewPager = viewPager;
            mViewPager.addOnPageChangeListener(mPagerListener);

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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(mPagerListener);
        }
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {}

        @Override
        public void onPageSelected(int position) {
            check(position);
        }
    }

    private void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;

        invalidate();
    }

    private void init(AttributeSet attrs) {
        setOnItemCheckedListener(index -> {
            if (mSelectedPosition != index) {
                mViewPager.setCurrentItem(index);
            }
        });
    }
}