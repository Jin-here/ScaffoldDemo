package com.vgaw.scaffold.view.tab.indicator;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.view.ViewGroup
import androidx.annotation.ColorInt
import com.vgaw.scaffold.R

class RoundRectSolidIndicator(context: Context, @ColorInt val color: Int = context.resources.getColor(R.color.colorAccent)) : BaseIndicator(context) {
    override fun init(count: Int) {
        val color: Int = getIndicatorColor()
        mIndicatorColors = IntArray(count)
        for (i in 0 until count) {
            mIndicatorColors[i] = color
        }
    }

    override fun onDraw(canvas: Canvas?, parent: ViewGroup?, selectedPos: Int, selectedOffset: Float) {
        val childCount = parent!!.childCount
        val height = parent!!.height
        val showIndicator = mIndicatorColors != null
        if (showIndicator) {
            if (childCount > 0) {
                val selectedTitle = parent!!.getChildAt(selectedPos)
                mSelectedIndicatorThickness = selectedTitle!!.width
                var left = selectedTitle.left
                var right = selectedTitle.right
                var color = mIndicatorColors[selectedPos]
                if (selectedOffset > 0f && selectedPos < childCount - 1) {
                    val nextColor = mIndicatorColors[selectedPos + 1]
                    if (color != nextColor) {
                        color = blendColors(nextColor, color, selectedOffset)
                    }

                    // Draw the selection partway between the tabs
                    val nextTitle = parent!!.getChildAt(selectedPos + 1)
                    left = (selectedOffset * nextTitle.left +
                            (1.0f - selectedOffset) * left).toInt()
                    right = (selectedOffset * nextTitle.right +
                            (1.0f - selectedOffset) * right).toInt()
                }
                mSelectedIndicatorPaint.color = color
                val indicatorWidth = selectedTitle!!.width
                val middle = (left + right) / 2
                val r = mSelectedIndicatorThickness / 2
                canvas!!.drawRoundRect(RectF((middle - indicatorWidth / 2).toFloat(), 0F,
                        (middle + indicatorWidth / 2).toFloat(), height.toFloat()), r.toFloat(), r.toFloat(), mSelectedIndicatorPaint)
            }
        }
    }

    @ColorInt fun getIndicatorColor(): Int {
        return color
    }

    /**
     * Blend `color1` and `color2` using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return `color1`, 0.5 will give an even blend,
     * 0.0 will return `color2`.
     */
    private fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
        val inverseRation = 1f - ratio
        val a = Color.alpha(color1) * ratio + Color.alpha(color2) * inverseRation
        val r = Color.red(color1) * ratio + Color.red(color2) * inverseRation
        val g = Color.green(color1) * ratio + Color.green(color2) * inverseRation
        val b = Color.blue(color1) * ratio + Color.blue(color2) * inverseRation
        return Color.argb(a.toInt(), r.toInt(), g.toInt(), b.toInt())
    }
}
