package com.vgaw.scaffold.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.util.phone.DensityUtil;

public class ImgTxtLayout extends LinearLayout {
    protected Drawable mIcon;
    protected Drawable mIconBackground;
    protected int mIconSize;
    protected String mName;
    protected int mNameColor;
    protected int mNameSize;
    protected int mPadding;

    private ImageView mImgTxtIv;
    private TextView mImgTxtTv;

    public ImgTxtLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ImgTxtLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImgTxtLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void setIcon(Drawable icon) {
        mImgTxtIv.setImageDrawable(icon);
    }

    public void setContent(String content) {
        mImgTxtTv.setText(content);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ImgTxtLayout);
        mIcon = array.getDrawable(R.styleable.ImgTxtLayout_imgTxtIcon);
        mIconBackground = array.getDrawable(R.styleable.ImgTxtLayout_imgTxtIconBackground);
        mIconSize = array.getDimensionPixelSize(R.styleable.ImgTxtLayout_imgTxtIconSize, DensityUtil.dp2px(getContext(), 48));
        mName = array.getString(R.styleable.ImgTxtLayout_imgTxtName);
        mNameColor = array.getColor(R.styleable.ImgTxtLayout_imgTxtNameColor, getResources().getColor(R.color.white));
        mNameSize = array.getDimensionPixelSize(R.styleable.ImgTxtLayout_imgTxtNameSize, DensityUtil.sp2px(getContext(), 16));
        mPadding = array.getDimensionPixelSize(R.styleable.ImgTxtLayout_imgTxtPadding, DensityUtil.sp2px(getContext(), 4));

        array.recycle();

        refreshChildParam();

        mImgTxtIv = new ImageView(getContext());
        mImgTxtIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImgTxtIv.setImageDrawable(mIcon);
        mImgTxtIv.setBackground(mIconBackground);
        addView(mImgTxtIv, proGravity(new LayoutParams(mIconSize, mIconSize)));

        mImgTxtTv = new TextView(getContext());
        mImgTxtTv.setText(mName);
        mImgTxtTv.setTextColor(mNameColor);
        mImgTxtTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNameSize);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        proPadding(layoutParams);
        addView(mImgTxtTv, proGravity(layoutParams));
    }

    private LayoutParams proPadding(LayoutParams params) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            params.leftMargin = mPadding;
        } else {
            params.topMargin = mPadding;
        }
        return params;
    }

    private LayoutParams proGravity(LayoutParams params) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            params.gravity = Gravity.CENTER_VERTICAL;
        } else {
            params.gravity = Gravity.CENTER_HORIZONTAL;
        }
        return params;
    }

    protected void refreshChildParam() {}
}
