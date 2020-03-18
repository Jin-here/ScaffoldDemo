package com.vgaw.scaffold.view.tab.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.ColorInt;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.util.phone.DensityUtil;
import com.vgaw.scaffold.view.tab.SlidingTabLayout;

public class RectIndicator extends BaseIndicator {
    public RectIndicator(Context context) {
        super(context);
    }

    @Override
    public void init(int count) {
        int color = getIndicatorColor();
        mIndicatorColors = new int[count];
        for (int i = 0;i < count;i++) {
            mIndicatorColors[i] = color;
        }

        mSelectedIndicatorThickness = DensityUtil.dp2px(mContext, 2);
    }

    @Override
    public void onDraw(Canvas canvas, SlidingTabLayout parent, int selectedPos, float selectedOffset) {
        int childCount = parent.getChildCount();
        int height = parent.getHeight();
        boolean showIndicator = (mIndicatorColors != null);
        if (showIndicator) {
            if (childCount > 0) {
                View selectedTitle = parent.getChildAt(selectedPos);
                int left = selectedTitle.getLeft();
                int right = selectedTitle.getRight();
                int color = mIndicatorColors[selectedPos];

                if (selectedOffset > 0f && selectedPos < (childCount - 1)) {
                    int nextColor = mIndicatorColors[selectedPos + 1];
                    if (color != nextColor) {
                        color = blendColors(nextColor, color, selectedOffset);
                    }

                    // Draw the selection partway between the tabs
                    View nextTitle = parent.getChildAt(selectedPos + 1);
                    left = (int) (selectedOffset * nextTitle.getLeft() +
                            (1.0f - selectedOffset) * left);
                    right = (int) (selectedOffset * nextTitle.getRight() +
                            (1.0f - selectedOffset) * right);
                }

                mSelectedIndicatorPaint.setColor(color);

                canvas.drawRect(new RectF(left, height - mSelectedIndicatorThickness, right, height), mSelectedIndicatorPaint);
            }
        }
    }

    protected @ColorInt int getIndicatorColor() {
        return mContext.getResources().getColor(R.color.black);
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
     *              0.0 will return {@code color2}.
     */
    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }
}
