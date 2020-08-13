package com.vgaw.scaffold.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;

class CheckedImageButton : AppCompatImageButton {
    private var mChecked: Boolean = false
    private var mNormalBkResId: Int = -1
    private var mCheckedBkResId: Int = -1
    private var mNormalImage: Drawable? = null
    private var mCheckedImage: Drawable? = null

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {
        init()
    }

    fun setPadding(padding: Int) {
        setPadding(padding, padding, padding, padding)
    }

    fun isChecked() = mChecked

    fun setChecked(push: Boolean) {
        this.mChecked = push;

        val image = if (push) mCheckedImage else mNormalImage
        if (image != null) {
            updateImage(image)
        }

        val background = if (push) mCheckedBkResId else mNormalBkResId
        if (background != -1) {
            updateBackground(background)
        }
    }

    fun setNormalBkResId(normalBkResId: Int) {
        this.mNormalBkResId = normalBkResId
        updateBackground(normalBkResId)
    }

    fun setCheckedBkResId(checkedBkResId: Int) {
        this.mCheckedBkResId = checkedBkResId
    }

    fun setNormalImageId(normalResId: Int) {
        mNormalImage = resources.getDrawable(normalResId)
        updateImage(this.mNormalImage!!)
    }

    fun setCheckedImageId(pushedResId: Int) {
        mCheckedImage = resources.getDrawable(pushedResId)
    }

    fun setNormalImage(bitmap: Bitmap) {
        this.mNormalImage = BitmapDrawable(resources, bitmap)
        updateImage(this.mNormalImage!!)
    }

    fun setCheckedImage(bitmap: Bitmap) {
        this.mCheckedImage = BitmapDrawable(resources, bitmap)
    }

    private fun updateBackground(resId: Int) {
        setBackgroundResource(resId)
    }

    private fun updateImage(drawable: Drawable) {
        setImageDrawable(drawable)
    }

    private fun init() {
        scaleType = ScaleType.CENTER_CROP
        setBackgroundColor(Color.TRANSPARENT)
    }
}
