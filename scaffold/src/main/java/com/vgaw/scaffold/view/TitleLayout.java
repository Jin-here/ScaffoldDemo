package com.vgaw.scaffold.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.util.phone.DensityUtil;

public class TitleLayout extends RelativeLayout {
    private ImageButton mTitleLayoutBack;
    private TextView mTitleLayoutTitle;
    private TextView mTitleLayoutMenu;

    private TitleLayout(Context context) {
        super(context);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = DensityUtil.dp2px(getContext(), 52);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    public void setBackClickListener(OnClickListener listener) {
        mTitleLayoutBack.setOnClickListener(listener);
    }

    public void setBackIcon(Drawable drawable) {
        mTitleLayoutBack.setImageDrawable(drawable);
    }

    public void setCaption(String title) {
        mTitleLayoutTitle.setText(title);
    }

    public void setMenu(String menu) {
        mTitleLayoutMenu.setText(menu);
    }

    public void setMenuEnabled(boolean enabled) {
        mTitleLayoutMenu.setEnabled(enabled);
        mTitleLayoutMenu.setTextColor(getResources().getColor(enabled ? R.color.black6 : R.color.black7));
    }
    public void setMenuClickListener(OnClickListener listener) {
        mTitleLayoutMenu.setOnClickListener(listener);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TitleLayout);
        String title = array.getString(R.styleable.TitleLayout_titleCaption);
        Drawable backIcon = array.getDrawable(R.styleable.TitleLayout_titleBackIcon);
        String menu = array.getString(R.styleable.TitleLayout_titleMenu);
        boolean menuEnabled = array.getBoolean(R.styleable.TitleLayout_titleMenuEnabled, false);
        boolean darkMode = array.getBoolean(R.styleable.TitleLayout_titleDarkMode, false);

        array.recycle();

        View view = View.inflate(getContext(), R.layout.title_layout, this);
        mTitleLayoutBack = view.findViewById(R.id.title_layout_back);
        mTitleLayoutTitle = view.findViewById(R.id.title_layout_title);
        mTitleLayoutMenu = view.findViewById(R.id.title_layout_menu);
        if (darkMode) {
            setBackgroundColor(getResources().getColor(R.color.dark));
            mTitleLayoutTitle.setTextAppearance(getContext(), R.style.H5_White_High_Left);
            mTitleLayoutMenu.setTextAppearance(getContext(), R.style.Subtitle1_White_High_Right);
        } else {
            setBackgroundColor(getResources().getColor(R.color.white));
            mTitleLayoutTitle.setTextAppearance(getContext(), R.style.H5_Black_High_Left);
            mTitleLayoutMenu.setTextAppearance(getContext(), R.style.Subtitle1_Black_High_Right);
        }

        setCaption(title);
        if (backIcon == null) {
            backIcon = getResources().getDrawable(darkMode ? R.drawable.back_white : R.drawable.back);
        }
        setBackIcon(backIcon);
        setMenu(menu);
        setMenuEnabled(menuEnabled);
    }
}
