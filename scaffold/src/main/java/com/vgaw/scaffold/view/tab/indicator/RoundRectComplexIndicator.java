package com.vgaw.scaffold.view.tab.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.util.phone.DensityUtil;

public class RoundRectComplexIndicator extends BaseIndicator {
    public RoundRectComplexIndicator(Context context) {
        super(context);
    }

    @Override
    public void init(int count) {
        int color = getIndicatorColor();
        mIndicatorColors = new int[count];
        for (int i = 0;i < count;i++) {
            mIndicatorColors[i] = color;
        }

        mSelectedIndicatorThickness = DensityUtil.dp2px(mContext, 4);
    }

    @Override
    public void onDraw(Canvas canvas, ViewGroup parent, int selectedPos, float selectedOffset) {
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

                int indicatorWidth = DensityUtil.dp2px(mContext, 20);
                int middle = (left + right) / 2;
                int r = (mSelectedIndicatorThickness / 2);
                // 绘制底边提示条
                canvas.drawRoundRect(new RectF(middle - (indicatorWidth / 2), height - mSelectedIndicatorThickness,
                        middle + (indicatorWidth / 2), height), r, r, mSelectedIndicatorPaint);

                // 绘制文字背景提示条
                int txtIndicatorMargin = DensityUtil.dp2px(mContext, 8

                );
                int txtIndicatorWidth = DensityUtil.dp2px(mContext, 74);
                int txtIndicatorR = DensityUtil.dp2px(mContext, 6);
                mSelectedIndicatorPaint.setColor(getTxtIndicatorColor());
                canvas.drawRoundRect(new RectF(middle - (txtIndicatorWidth / 2), txtIndicatorMargin,
                        middle + (txtIndicatorWidth / 2), height - txtIndicatorMargin), txtIndicatorR, txtIndicatorR, mSelectedIndicatorPaint);
            }
        }
    }

    protected @ColorInt int getTxtIndicatorColor() {
        return mContext.getResources().getColor(R.color.tab_txt_bg);
    }

    protected @ColorInt int getIndicatorColor() {
        return mContext.getResources().getColor(R.color.colorAccent);
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
