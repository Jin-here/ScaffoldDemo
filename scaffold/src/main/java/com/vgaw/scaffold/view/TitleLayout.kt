package com.vgaw.scaffold.view;

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.Util
import com.vgaw.scaffold.util.phone.DensityUtil

class TitleLayout : RelativeLayout {
    private lateinit var mTitleLayoutBack: ImageButton
    private lateinit var mTitleLayoutTitle: TextView
    private lateinit var mTitleLayoutMenu: TextView
    private lateinit var mTitleLayoutMenuIcon: ImageButton

    private constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = DensityUtil.dp2px(context, 52F)
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }

    fun setBackClickListener(listener: (View) -> Unit) {
        mTitleLayoutBack.setOnClickListener(listener)
    }

    fun setMenuClickListener(listener: (View) -> Unit) {
        mTitleLayoutMenu.setOnClickListener(listener)
    }

    fun setBackIcon(drawable: Drawable?) {
        mTitleLayoutBack.setImageDrawable(drawable)
    }

    fun setCaption(title: String?) {
        mTitleLayoutTitle.text = Util.nullToEmpty(title)
    }

    fun setMenu(menu: String?) {
        mTitleLayoutMenu.text = Util.nullToEmpty(menu)
    }

    fun setMenuEnabled(enabled: Boolean) {
        mTitleLayoutMenu.isEnabled = enabled
        mTitleLayoutMenu.setTextColor(resources.getColor(if (enabled) R.color.black6 else R.color.black7))
    }

    fun setMenuIcon(drawable: Drawable?) {
        mTitleLayoutMenuIcon.setImageDrawable(drawable)
    }

    fun setMenuIconEnabled(enabled: Boolean) {
        mTitleLayoutMenuIcon.isEnabled = enabled
    }

    private fun init(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout)
        val title = array.getString(R.styleable.TitleLayout_titleCaption)
        var backIcon = array.getDrawable(R.styleable.TitleLayout_titleBackIcon)
        val menu = array.getString(R.styleable.TitleLayout_titleMenu)
        val menuEnabled = array.getBoolean(R.styleable.TitleLayout_titleMenuEnabled, false)
        val darkMode = array.getBoolean(R.styleable.TitleLayout_titleDarkMode, false)
        val menuIconEnabled = array.getBoolean(R.styleable.TitleLayout_titleMenuIconEnabled, false)
        var menuIcon = array.getDrawable(R.styleable.TitleLayout_titleMenuIcon)

        array.recycle()

        val view = View.inflate(context, R.layout.title_layout, this)
        mTitleLayoutBack = view.findViewById(R.id.title_layout_back)
        mTitleLayoutTitle = view.findViewById(R.id.title_layout_title)
        mTitleLayoutMenu = view.findViewById(R.id.title_layout_menu)
        mTitleLayoutMenuIcon = view.findViewById(R.id.title_layout_menu_icon)

        if (darkMode) {
            setBackgroundColor(getResources().getColor(R.color.dark))
            mTitleLayoutTitle.setTextAppearance(getContext(), R.style.H5_White_High_Left)
            mTitleLayoutMenu.setTextAppearance(getContext(), R.style.Subtitle1_White_High_Right)
        } else {
            setBackgroundColor(getResources().getColor(R.color.white))
            mTitleLayoutTitle.setTextAppearance(getContext(), R.style.H5_Black_High_Left)
            mTitleLayoutMenu.setTextAppearance(getContext(), R.style.Subtitle1_Black_High_Right)
        }

        setCaption(title)
        if (backIcon == null) {
            backIcon = resources.getDrawable(if (darkMode) R.drawable.back_white else R.drawable.back)
        }
        setBackIcon(backIcon)
        setMenu(menu)
        setMenuEnabled(menuEnabled)
    }
}
