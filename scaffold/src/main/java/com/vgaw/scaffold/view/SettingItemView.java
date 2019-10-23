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

public class SettingItemView extends RelativeLayout {
    private TextView mSettingItemLayoutTitle;
    private ImageView mSettingItemLayoutIcon;
    private TextView mSettingItemLayoutDes;

    private SettingItemView(Context context) {
        super(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void setOnItemClickListener(OnClickListener clickListener) {
        setOnClickListener(clickListener);
    }

    public void setTitle(String title) {
        mSettingItemLayoutTitle.setText(title);
    }

    public void setIcon(Drawable icon) {
        if (icon == null) {
            mSettingItemLayoutIcon.setVisibility(GONE);
        } else {
            mSettingItemLayoutIcon.setImageDrawable(icon);
            mSettingItemLayoutIcon.setVisibility(VISIBLE);
        }
    }

    public void setDes(String des) {
        mSettingItemLayoutDes.setText(des);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        String title = array.getString(R.styleable.SettingItemView_settingName);
        Drawable icon = array.getDrawable(R.styleable.SettingItemView_settingIcon);
        String des = array.getString(R.styleable.SettingItemView_settingDes);

        array.recycle();

        View view = View.inflate(getContext(), R.layout.setting_item_layout, this);
        mSettingItemLayoutTitle = view.findViewById(R.id.setting_item_layout_title);
        mSettingItemLayoutIcon = view.findViewById(R.id.setting_item_layout_icon);
        mSettingItemLayoutDes = view.findViewById(R.id.setting_item_layout_des);

        setTitle(title);
        setIcon(icon);
        setDes(des);
    }
}
