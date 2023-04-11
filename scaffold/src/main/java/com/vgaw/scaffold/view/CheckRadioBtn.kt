package com.vgaw.scaffold.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.Util
import com.vgaw.scaffold.view.checkable.CheckableItemInterface

class CheckRadioBtn: LinearLayout, CheckableItemInterface {
    private lateinit var mCheckBtnToggle: CheckedImageButton
    private lateinit var mCheckBtnTitle: TextView

    private constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {
        init(attrs)
    }

    fun hideTitle(hideTitle: Boolean) {
        mCheckBtnTitle.visibility = if (hideTitle) View.GONE else View.VISIBLE
    }

    fun setTitle(title: String?) {
        mCheckBtnTitle.text = Util.nullToEmpty(title)
    }

    fun check(check: Boolean) {
        mCheckBtnToggle.setChecked(check)
    }

    fun checked() = mCheckBtnToggle.isChecked()

    private fun init(attrs: AttributeSet) {
        setOnClickListener {
            check(!checked())
        }
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        val array = context.obtainStyledAttributes(attrs, R.styleable.CheckRadioBtn)
        val title = array.getString(R.styleable.CheckRadioBtn_checkRadioTitle)
        val hideTitle = array.getBoolean(R.styleable.CheckRadioBtn_checkRadioHideTitle, false)
        val multiChoice = array.getBoolean(R.styleable.CheckRadioBtn_checkRadioMultiChoice, false)

        array.recycle()

        val view = View.inflate(context, R.layout.check_radio_btn_layout, this)
        mCheckBtnToggle = view.findViewById(R.id.check_radio_btn_toggle)
        mCheckBtnTitle = view.findViewById(R.id.check_radio_btn_title)

        mCheckBtnToggle.isClickable = false
        if (multiChoice) {
            mCheckBtnToggle.setNormalImageId(R.drawable.check_normal)
            mCheckBtnToggle.setCheckedImageId(R.drawable.check_checked)
        } else {
            mCheckBtnToggle.setNormalImageId(R.drawable.radio_normal)
            mCheckBtnToggle.setCheckedImageId(R.drawable.radio_checked)
        }

        check(false)
        hideTitle(hideTitle)
        setTitle(title)
    }

    override fun onCheckChanged(checkedNow: Boolean) {
        check(checkedNow)
    }
}