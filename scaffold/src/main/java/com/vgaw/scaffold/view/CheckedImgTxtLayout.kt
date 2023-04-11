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
import androidx.annotation.DrawableRes
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.Util
import com.vgaw.scaffold.util.phone.DensityUtil

open class CheckedImgTxtLayout : LinearLayout {
    protected var mIcon: Int = -1
    protected var mIconOn: Int = -1
    protected var mIconBackground: Drawable? = null
    protected var mIconSize: Int = -1
    protected var mName: String? = null
    protected var mNameColor: Int = -1
    protected var mNameSize: Int = -1
    protected var mPadding: Int = -1

    private lateinit var mImgTxtIv: CheckedImageButton
    private lateinit var mImgTxtTv: TextView

    constructor(context: Context): super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        mImgTxtIv.setNormalImageId(iconRes)
    }

    fun setIconOn(@DrawableRes iconRes: Int) {
        mImgTxtIv.setCheckedImageId(iconRes)
    }

    fun setContent(content: String?) {
        mImgTxtTv.text = Util.nullToEmpty(content)
    }

    fun setContentColor(@ColorInt color: Int) {
        mImgTxtTv.setTextColor(color)
    }

    protected fun refreshChildParam() {}

    private fun init(attrs: AttributeSet?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            foreground = resources.getDrawable(R.drawable.selectable_item_bg)
        }
        if (attrs != null) {
            val array = getContext().obtainStyledAttributes(attrs, R.styleable.CheckedImgTxtLayout)
            mIcon = array.getInt(R.styleable.CheckedImgTxtLayout_checkedImgTxtIcon, -1)
            mIconOn = array.getInt(R.styleable.CheckedImgTxtLayout_checkedImgTxtIconOn, -1)
            mIconBackground = array.getDrawable(R.styleable.CheckedImgTxtLayout_checkedImgTxtIconBackground)
            mIconSize = array.getDimensionPixelSize(R.styleable.CheckedImgTxtLayout_checkedImgTxtIconSize, DensityUtil.dp2px(getContext(), 48F))
            mName = array.getString(R.styleable.CheckedImgTxtLayout_checkedImgTxtName)
            mNameColor = array.getColor(R.styleable.CheckedImgTxtLayout_checkedImgTxtNameColor, getResources().getColor(R.color.white))
            mNameSize = array.getDimensionPixelSize(R.styleable.CheckedImgTxtLayout_checkedImgTxtNameSize, DensityUtil.sp2px(getContext(), 16F))
            mPadding = array.getDimensionPixelSize(R.styleable.CheckedImgTxtLayout_checkedImgTxtPadding, DensityUtil.sp2px(getContext(), 4F))

            array.recycle()
        }

        refreshChildParam()

        mImgTxtIv = CheckedImageButton(context)
        mImgTxtIv.scaleType = ImageView.ScaleType.CENTER_CROP
        if (mIcon != -1) {
            mImgTxtIv.setNormalImageId(mIcon)
        }
        if (mIconOn != -1) {
            mImgTxtIv.setCheckedImageId(mIconOn)
        }
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
