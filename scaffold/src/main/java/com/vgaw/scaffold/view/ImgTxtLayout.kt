package com.vgaw.scaffold.view;

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.Util
import com.vgaw.scaffold.util.phone.DensityUtil

open class ImgTxtLayout : LinearLayout {
    protected var mIcon: Drawable? = null
    protected var mIconBackground: Drawable? = null
    protected var mIconSize: Int = -1
    protected var mName: String? = null
    protected var mNameColor: Int = -1
    protected var mNameSize: Int = -1
    protected var mPadding: Int = -1

    private lateinit var mImgTxtIv: ImageView
    private lateinit var mImgTxtTv: TextView

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setIcon(icon: Drawable?) {
        mImgTxtIv.setImageDrawable(icon)
    }

    fun setContent(content: String?) {
        mImgTxtTv.text = Util.nullToEmpty(content)
    }

    fun setContentColor(@ColorInt color: Int) {
        mImgTxtTv.setTextColor(color)
    }

    protected fun refreshChildParam() {}

    private fun init(attrs: AttributeSet) {
        val array = getContext().obtainStyledAttributes(attrs, R.styleable.ImgTxtLayout)
        val ignoreSelectWave = array.getBoolean(R.styleable.ImgTxtLayout_imgTxtIgnoreSelectWave, false)
        if (!ignoreSelectWave) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                foreground = resources.getDrawable(R.drawable.selectable_item_bg)
            }
        }
        mIcon = array.getDrawable(R.styleable.ImgTxtLayout_imgTxtIcon)
        mIconBackground = array.getDrawable(R.styleable.ImgTxtLayout_imgTxtIconBackground)
        mIconSize = array.getDimensionPixelSize(R.styleable.ImgTxtLayout_imgTxtIconSize, DensityUtil.dp2px(getContext(), 48F))
        mName = array.getString(R.styleable.ImgTxtLayout_imgTxtName)
        mNameColor = array.getColor(R.styleable.ImgTxtLayout_imgTxtNameColor, getResources().getColor(R.color.white))
        mNameSize = array.getDimensionPixelSize(R.styleable.ImgTxtLayout_imgTxtNameSize, DensityUtil.sp2px(getContext(), 16F))
        mPadding = array.getDimensionPixelSize(R.styleable.ImgTxtLayout_imgTxtPadding, DensityUtil.sp2px(getContext(), 4F))

        array.recycle()

        refreshChildParam()

        mImgTxtIv = ImageView(context)
        mImgTxtIv.scaleType = ImageView.ScaleType.CENTER_CROP
        mImgTxtIv.setImageDrawable(mIcon)
        mImgTxtIv.background = mIconBackground
        addView(mImgTxtIv, proGravity(LayoutParams(mIconSize, mIconSize)))

        mImgTxtTv = TextView(context)
        mImgTxtTv.text = mName
        mImgTxtTv.setTextColor(mNameColor)
        mImgTxtTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNameSize.toFloat())
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        proPadding(layoutParams)
        addView(mImgTxtTv, proGravity(layoutParams))
    }

    private fun proPadding(params: LayoutParams): LayoutParams {
        if (orientation == HORIZONTAL) {
            params.leftMargin = mPadding
        } else {
            params.topMargin = mPadding
        }
        return params
    }

    private fun proGravity(params: LayoutParams): LayoutParams {
        if (orientation == HORIZONTAL) {
            params.gravity = Gravity.CENTER_VERTICAL
        } else {
            params.gravity = Gravity.CENTER_HORIZONTAL
        }
        return params
    }
}
