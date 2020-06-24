package com.vgaw.scaffold.view;

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.phone.DensityUtil

class TxtTxtLayout : LinearLayout {
    private lateinit var mTxtTxtUp: TextView
    private lateinit var mTxtTxtBelow: TextView

    constructor(context: Context): super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setUpText(up: String) {
        mTxtTxtUp.setText(up)
    }

    private fun init(attrs: AttributeSet?) {
        orientation = VERTICAL
        gravity = Gravity.CENTER

        val view = View.inflate(context, R.layout.txt_txt_layout, this)
        mTxtTxtUp = view.findViewById(R.id.txt_txt_up);
        mTxtTxtBelow = view.findViewById(R.id.txt_txt_below);

        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.TxtTxtLayout)
            val txtUp = array.getString(R.styleable.TxtTxtLayout_txtUp)
            val txtBelow = array.getString(R.styleable.TxtTxtLayout_txtBelow)
            val txtUpSize = array.getDimensionPixelSize(R.styleable.TxtTxtLayout_txtUpSize, 0)
            val txtBelowSize = array.getDimensionPixelSize(R.styleable.TxtTxtLayout_txtBelowSize, 0)
            val txtUpColor = array.getColor(R.styleable.TxtTxtLayout_txtUpColor, Color.BLACK)
            val txtBelowColor = array.getColor(R.styleable.TxtTxtLayout_txtBelowColor, Color.BLACK)
            val padding = array.getDimensionPixelSize(R.styleable.TxtTxtLayout_txtPadding, 0)

            array.recycle()

            mTxtTxtUp.text = txtUp
            mTxtTxtUp.textSize = DensityUtil.px2sp(context, txtUpSize.toFloat()).toFloat()
            mTxtTxtUp.setTextColor(txtUpColor)

            mTxtTxtBelow.text = txtBelow
            mTxtTxtBelow.textSize = DensityUtil.px2sp(context, txtBelowSize.toFloat()).toFloat()
            mTxtTxtBelow.setTextColor(txtBelowColor)
            val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = padding
            mTxtTxtBelow.layoutParams = layoutParams
        }
    }
}
