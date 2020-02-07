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
import com.vgaw.scaffold.util.phone.DensityUtil;

public class SettingToggleView extends RelativeLayout {
    private ImageView mSetToggleIcon;
    private TextView mSetToggleTitle;
    private TextView mSetToggleDes;
    private CheckedImageButton mSetToggleSw;

    private OnCheckChangedListener mListener;

    public SettingToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SettingToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setTitle(String title) {
        mSetToggleTitle.setText(title);
    }

    public void setDescription(String description) {
        mSetToggleDes.setText(description);
    }

    public void check(boolean check) {
        mSetToggleSw.setChecked(check);
    }

    public boolean checked() {
        return mSetToggleSw.isChecked();
    }

    public void setIcon(Drawable icon) {
        if (icon == null) {
            mSetToggleIcon.setVisibility(GONE);
        } else {
            mSetToggleIcon.setImageDrawable(icon);
            mSetToggleIcon.setVisibility(VISIBLE);
        }
    }

    public void setOnCheckChangeListener(OnCheckChangedListener listener) {
        mListener = listener;
    }

    private void init(AttributeSet attrs) {
        View view = View.inflate(getContext(), R.layout.setting_toggle_layout, this);
        mSetToggleIcon = view.findViewById(R.id.setting_toggle_icon);
        mSetToggleTitle = view.findViewById(R.id.setting_toggle_title);
        mSetToggleDes = view.findViewById(R.id.setting_toggle_des);
        mSetToggleSw = view.findViewById(R.id.setting_toggle_sw);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SettingToggleView);
        String title = array.getString(R.styleable.SettingToggleView_settingToggleName);
        String des = array.getString(R.styleable.SettingToggleView_settingToggleDes);
        boolean checked = array.getBoolean(R.styleable.SettingToggleView_settingToggled, false);
        Drawable icon = array.getDrawable(R.styleable.SettingToggleView_settingToggleIcon);
        int style = array.getInt(R.styleable.SettingToggleView_settingToggleStyle, 0);

        array.recycle();

        if (style == 0) {
            mSetToggleSw.setCheckedImageId(R.drawable.toggle_on);
            mSetToggleSw.setNormalImageId(R.drawable.toggle_off);
        } else {
            mSetToggleSw.setCheckedImageId(R.drawable.check_box_on);
            mSetToggleSw.setNormalImageId(R.drawable.check_box_off);
            mSetToggleSw.setPadding(DensityUtil.dp2px(getContext(), 6));
        }
        mSetToggleSw.setOnClickListener(v -> onClicked());
        setOnClickListener(v -> onClicked());

        setTitle(title);
        setDescription(des);
        check(checked);
        setIcon(icon);
    }

    private void onClicked() {
        boolean doneByUser = false;
        if (mListener != null) {
            doneByUser = mListener.onCheckChanged(checked());
        }
        if (!doneByUser) {
            check(!checked());
        }
    }
}
