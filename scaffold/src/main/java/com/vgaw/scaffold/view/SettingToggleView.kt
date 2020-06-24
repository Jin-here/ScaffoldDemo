package com.vgaw.scaffold.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.util.Util
import com.vgaw.scaffold.util.phone.DensityUtil;

class SettingToggleView : RelativeLayout {
    private lateinit var mSetToggleIcon: ImageView
    private lateinit var mSetToggleTitle: TextView
    private lateinit var mSetToggleDes: TextView
    private lateinit var mSetToggleSw: CheckedImageButton

    private var mListener: OnCheckChangedListener? = null

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setTitle(title: String?) {
        mSetToggleTitle.text = Util.nullToEmpty(title)
    }

    fun setDescription(description: String?) {
        mSetToggleDes.text = Util.nullToEmpty(description)
    }

    fun check(check: Boolean) {
        mSetToggleSw.setChecked(check)
    }

    fun checked() = mSetToggleSw.isChecked()

    fun setIcon(icon: Drawable?) {
        if (icon == null) {
            mSetToggleIcon.visibility = GONE
        } else {
            mSetToggleIcon.setImageDrawable(icon)
            mSetToggleIcon.visibility = VISIBLE
        }
    }

    fun setOnCheckChangeListener(listener: OnCheckChangedListener?) {
        mListener = listener
    }

    private fun init(attrs: AttributeSet) {
        val view = View.inflate(getContext(), R.layout.setting_toggle_layout, this)
        mSetToggleIcon = view.findViewById(R.id.setting_toggle_icon)
        mSetToggleTitle = view.findViewById(R.id.setting_toggle_title)
        mSetToggleDes = view.findViewById(R.id.setting_toggle_des)
        mSetToggleSw = view.findViewById(R.id.setting_toggle_sw)

        val array = getContext().obtainStyledAttributes(attrs, R.styleable.SettingToggleView)
        val title = array.getString(R.styleable.SettingToggleView_settingToggleName)
        val des = array.getString(R.styleable.SettingToggleView_settingToggleDes)
        val checked = array.getBoolean(R.styleable.SettingToggleView_settingToggled, false)
        val icon = array.getDrawable(R.styleable.SettingToggleView_settingToggleIcon)
        val style = array.getInt(R.styleable.SettingToggleView_settingToggleStyle, 0)

        array.recycle()

        if (style == 0) {
            mSetToggleSw.setCheckedImageId(R.drawable.toggle_on)
            mSetToggleSw.setNormalImageId(R.drawable.toggle_off)
        } else {
            mSetToggleSw.setCheckedImageId(R.drawable.check_box_on)
            mSetToggleSw.setNormalImageId(R.drawable.check_box_off)
            mSetToggleSw.setPadding(DensityUtil.dp2px(context, 6F))
        }
        mSetToggleSw.setOnClickListener { onClicked() }
        setOnClickListener { onClicked() }

        setTitle(title)
        setDescription(des)
        check(checked)
        setIcon(icon)
    }

    private fun onClicked() {
        var doneByUser = false
        if (mListener != null) {
            doneByUser = mListener!!.onCheckChanged(checked())
        }
        if (!doneByUser) {
            check(!checked())
        }
    }
}
