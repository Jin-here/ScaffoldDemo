package com.vgaw.scaffold.view;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.phone.DensityUtil

class DividerView : View {
    private var mOrientation: Int = LinearLayout.VERTICAL
    private var mPaddingLeft: Int = 0
    private var mPaddingRight: Int = 0

    private lateinit var mPaint: Paint

    private constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = MeasureSpec.makeMeasureSpec(DensityUtil.dp2px(context, 0.5F), MeasureSpec.EXACTLY)
        if (mOrientation == LinearLayout.VERTICAL) {
            super.onMeasure(widthMeasureSpec, size)
        } else {
            super.onMeasure(size, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0
        if (mOrientation == LinearLayout.VERTICAL) {
            left = mPaddingLeft
            right = width - mPaddingRight
            bottom = height
        } else {
            top = mPaddingLeft
            right = width
            bottom = height - mPaddingRight
        }
        canvas?.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
    }

    private fun init(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.DividerView)
        mOrientation = array.getInteger(R.styleable.DividerView_dividerOrientation, LinearLayout.VERTICAL)
        val leftPadding = array.getDimensionPixelSize(R.styleable.DividerView_dividerLeftPadding, 0)
        val rightPadding = array.getDimensionPixelSize(R.styleable.DividerView_dividerRightPadding, 0)
        val padding = array.getDimensionPixelSize(R.styleable.DividerView_dividerPadding, 0)

        array.recycle()

        mPaddingLeft = leftPadding
        mPaddingRight = rightPadding
        if (mPaddingLeft == 0) {
            mPaddingLeft = padding
        }
        if (mPaddingRight == 0) {
            mPaddingRight = padding
        }

        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.color = resources.getColor(R.color.divider_bg)
    }
}
