package com.vgaw.scaffold.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by xch on 2016/10/25.
 */

class MarqueeTextView : AppCompatTextView {
    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    override fun isFocused() = true

    private fun init() {
        ellipsize = TextUtils.TruncateAt.MARQUEE
        setSingleLine()
    }
}