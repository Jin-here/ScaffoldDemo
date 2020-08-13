package com.vgaw.scaffold.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.Util

class SettingItemView : LinearLayout {
    private lateinit var mSettingItemLayoutTitle: TextView
    private lateinit var mSettingItemLayoutIcon: ImageView
    private lateinit var mSettingItemLayoutDes: TextView
    private lateinit var mSettingItemLayoutArrow: View

    private constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setOnItemClickListener(clickListener: OnClickListener) {
        setOnClickListener(clickListener)
    }

    fun setTitle(title: String?) {
        mSettingItemLayoutTitle.text = Util.nullToEmpty(title)
    }

    fun setIcon(icon: Drawable?) {
        if (icon == null) {
            mSettingItemLayoutIcon.setVisibility(GONE)
        } else {
            mSettingItemLayoutIcon.setImageDrawable(icon)
            mSettingItemLayoutIcon.setVisibility(VISIBLE)
        }
    }

    fun setDes(des: String?) {
        mSettingItemLayoutDes.text = Util.nullToEmpty(des)
    }

    fun hideArrow(hideArrow: Boolean) {
        mSettingItemLayoutArrow.visibility = if (hideArrow) View.GONE else View.VISIBLE
    }

    private fun init(attrs: AttributeSet) {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        val array = getContext().obtainStyledAttributes(attrs, R.styleable.SettingItemView)
        val title = array.getString(R.styleable.SettingItemView_settingName)
        val icon = array.getDrawable(R.styleable.SettingItemView_settingIcon)
        val des = array.getString(R.styleable.SettingItemView_settingDes)
        val hideArrow = array.getBoolean(R.styleable.SettingItemView_settingHideArrow, false)

        array.recycle();

        val view = View.inflate(getContext(), R.layout.setting_item_layout, this)
        mSettingItemLayoutTitle = view.findViewById(R.id.setting_item_layout_title)
        mSettingItemLayoutIcon = view.findViewById(R.id.setting_item_layout_icon)
        mSettingItemLayoutDes = view.findViewById(R.id.setting_item_layout_des)
        mSettingItemLayoutArrow = view.findViewById(R.id.setting_item_layout_arrow)

        setTitle(title)
        setIcon(icon)
        setDes(des)
        hideArrow(hideArrow)
    }
}
