package com.vgaw.scaffold.view.tab.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.ViewGroup;

public abstract class BaseIndicator {
    protected int mSelectedIndicatorThickness;
    protected Paint mSelectedIndicatorPaint;
    protected int[] mIndicatorColors;

    protected Context mContext;

    public BaseIndicator(Context context) {
        mContext = context;

        mSelectedIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public abstract void init(int count);

    public abstract void onDraw(Canvas canvas, ViewGroup parent, int selectedPos, float selectedOffset);
}
