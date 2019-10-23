package com.vgaw.scaffold.view.bottomnavigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.util.phone.DensityUtil;

public class BottomNavigationLayout extends LinearLayout {
    private OnItemCheckedListener mListener;

    public BottomNavigationLayout(Context context) {
        super(context);
        init();
    }

    public BottomNavigationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void check(int index) {
        ((BottomNavigationItem) getChildAt(index)).check(true);
    }

    public void showBubble(int index) {
        ((BottomNavigationItem) getChildAt(index)).showBubble(true);
    }

    public void clearBubble(int index) {
        ((BottomNavigationItem) getChildAt(index)).showBubble(false);
    }

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        mListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int size = getChildCount();
        for (int i = 0;i < size;i++) {
            View child = getChildAt(i);
            if (child instanceof BottomNavigationItem) {
                final int finalI = i;
                ((BottomNavigationItem) child).setOnItemCheckedListener(new OnItemCheckedListener() {
                    @Override
                    public void onItemChecked(int index) {
                        onOnItemChecked(finalI);

                        callListener(finalI);
                    }
                });
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(DensityUtil.dp2px(getContext(), 48), MeasureSpec.EXACTLY));
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void onOnItemChecked(int index) {
        int size = getChildCount();
        for (int i = 0;i < size;i++) {
            if (i != index) {
                View child = getChildAt(i);
                if (child instanceof BottomNavigationItem) {
                    ((BottomNavigationItem) child).check(false);
                }
            }
        }
    }

    private void callListener(int index) {
        if (mListener != null) {
            mListener.onItemChecked(index);
        }
    }
}
