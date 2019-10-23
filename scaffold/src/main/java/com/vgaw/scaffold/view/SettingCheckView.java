package com.vgaw.scaffold.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.vgaw.scaffold.R;

public class SettingCheckView extends RelativeLayout {
    private ImageView mSetCheckIcon;
    private TextView mSetCheckTitle;
    private TextView mSetCheckDes;
    private MaterialCheckBox mSetCheckBox;

    public SettingCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SettingCheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setTitle(String title) {
        mSetCheckTitle.setText(title);
    }

    public void setDescription(String description) {
        mSetCheckDes.setText(description);
    }

    public void check(boolean check) {
        mSetCheckBox.setChecked(check);
    }

    public boolean checked() {
        return mSetCheckBox.isChecked();
    }

    public void setIcon(Drawable icon) {
        if (icon == null) {
            mSetCheckIcon.setVisibility(GONE);
        } else {
            mSetCheckIcon.setImageDrawable(icon);
            mSetCheckIcon.setVisibility(VISIBLE);
        }
    }

    public void setOnCheckChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mSetCheckBox.setOnCheckedChangeListener(listener);
    }

    private void init(AttributeSet attrs) {
        View view = View.inflate(getContext(), R.layout.setting_check_layout, this);
        mSetCheckTitle = view.findViewById(R.id.setting_check_title);
        mSetCheckIcon = view.findViewById(R.id.setting_check_icon);
        mSetCheckDes = view.findViewById(R.id.setting_check_des);
        mSetCheckBox = view.findViewById(R.id.setting_check_box);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                check(!checked());
            }
        });

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SettingCheckView);
        String title = array.getString(R.styleable.SettingCheckView_settingCheckName);
        String des = array.getString(R.styleable.SettingCheckView_settingCheckDes);
        boolean checked = array.getBoolean(R.styleable.SettingCheckView_settingChecked, false);
        Drawable icon = array.getDrawable(R.styleable.SettingCheckView_settingCheckIcon);

        array.recycle();

        setTitle(title);
        setDescription(des);
        check(checked);
        setIcon(icon);
    }
}
