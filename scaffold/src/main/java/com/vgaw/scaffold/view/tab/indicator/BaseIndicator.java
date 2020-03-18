package com.vgaw.scaffold.view.tab.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.vgaw.scaffold.view.tab.SlidingTabLayout;

public abstract class BaseIndicator {
    protected int mSelectedIndicatorThickness;
    protected Paint mSelectedIndicatorPaint;
    protected int[] mIndicatorColors;

    protected Context mContext;

    public BaseIndicator(Context context) {
        mContext = context;

        mSelectedIndicatorPaint = new Paint();
    }

    public abstract void init(int count);

    public abstract void onDraw(Canvas canvas, SlidingTabLayout parent, int selectedPos, float selectedOffset);
}
