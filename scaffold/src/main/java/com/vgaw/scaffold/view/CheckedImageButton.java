package com.vgaw.scaffold.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;

public class CheckedImageButton extends AppCompatImageButton {

    private boolean checked;

    private int normalBkResId;

    private int checkedBkResId;

    private Drawable normalImage;

    private Drawable checkedImage;

    public CheckedImageButton(Context context) {
        super(context);
        init();
    }

    public CheckedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckedImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setPadding(int padding) {
        setPadding(padding, padding, padding, padding);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean push) {
        this.checked = push;

        Drawable image = push ? checkedImage : normalImage;
        if (image != null) {
            updateImage(image);
        }

        int background = push ? checkedBkResId : normalBkResId;
        if (background != 0) {
            updateBackground(background);
        }
    }

    public void setNormalBkResId(int normalBkResId) {
        this.normalBkResId = normalBkResId;
        updateBackground(normalBkResId);
    }

    public void setCheckedBkResId(int checkedBkResId) {
        this.checkedBkResId = checkedBkResId;
    }

    public void setNormalImageId(int normalResId) {
        normalImage = getResources().getDrawable(normalResId);
        updateImage(normalImage);
    }

    public void setCheckedImageId(int pushedResId) {
        checkedImage = getResources().getDrawable(pushedResId);
    }

    public void setNormalImage(Bitmap bitmap) {
        this.normalImage = new BitmapDrawable(getResources(), bitmap);
        updateImage(this.normalImage);
    }

    public void setCheckedImage(Bitmap bitmap) {
        this.checkedImage = new BitmapDrawable(getResources(), bitmap);
    }

    private void updateBackground(int resId) {
        setBackgroundResource(resId);
    }

    private void updateImage(Drawable drawable) {
        setImageDrawable(drawable);
    }

    private void init() {
        setScaleType(ScaleType.CENTER_CROP);
        setBackgroundColor(Color.TRANSPARENT);
    }
}
