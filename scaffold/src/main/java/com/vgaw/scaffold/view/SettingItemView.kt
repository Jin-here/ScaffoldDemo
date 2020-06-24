package com.vgaw.scaffold.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.util.Util

class SettingItemView : RelativeLayout {
    private lateinit var mSettingItemLayoutTitle: TextView
    private lateinit var mSettingItemLayoutIcon: ImageView
    private lateinit var mSettingItemLayoutDes: TextView

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

    private fun init(attrs: AttributeSet) {
        val array = getContext().obtainStyledAttributes(attrs, R.styleable.SettingItemView)
        val title = array.getString(R.styleable.SettingItemView_settingName)
        val icon = array.getDrawable(R.styleable.SettingItemView_settingIcon)
        val des = array.getString(R.styleable.SettingItemView_settingDes)

        array.recycle();

        val view = View.inflate(getContext(), R.layout.setting_item_layout, this)
        mSettingItemLayoutTitle = view.findViewById(R.id.setting_item_layout_title)
        mSettingItemLayoutIcon = view.findViewById(R.id.setting_item_layout_icon)
        mSettingItemLayoutDes = view.findViewById(R.id.setting_item_layout_des)

        setTitle(title)
        setIcon(icon)
        setDes(des)
    }
}
