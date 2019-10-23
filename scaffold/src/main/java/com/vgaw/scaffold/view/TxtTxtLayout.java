package com.vgaw.scaffold.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.util.phone.DensityUtil;

public class TxtTxtLayout extends LinearLayout {
    private TextView mTxtTxtUp;
    private TextView mTxtTxtBelow;

    public TxtTxtLayout(Context context) {
        super(context);
        init(null);
    }

    public TxtTxtLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TxtTxtLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setUpText(String up) {
        mTxtTxtUp.setText(up);
    }

    private void init(AttributeSet attrs) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        View view = View.inflate(getContext(), R.layout.txt_txt_layout, this);
        mTxtTxtUp = view.findViewById(R.id.txt_txt_up);
        mTxtTxtBelow = view.findViewById(R.id.txt_txt_below);

        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TxtTxtLayout);
            String txtUp = array.getString(R.styleable.TxtTxtLayout_txtUp);
            String txtBelow = array.getString(R.styleable.TxtTxtLayout_txtBelow);
            int txtUpSize = array.getDimensionPixelSize(R.styleable.TxtTxtLayout_txtUpSize, 0);
            int txtBelowSize = array.getDimensionPixelSize(R.styleable.TxtTxtLayout_txtBelowSize, 0);
            int txtUpColor = array.getColor(R.styleable.TxtTxtLayout_txtUpColor, Color.BLACK);
            int txtBelowColor = array.getColor(R.styleable.TxtTxtLayout_txtBelowColor, Color.BLACK);
            int padding = array.getDimensionPixelSize(R.styleable.TxtTxtLayout_txtPadding, 0);

            array.recycle();

            mTxtTxtUp.setText(txtUp);
            mTxtTxtUp.setTextSize(DensityUtil.px2sp(getContext(), txtUpSize));
            mTxtTxtUp.setTextColor(txtUpColor);

            mTxtTxtBelow.setText(txtBelow);
            mTxtTxtBelow.setTextSize(DensityUtil.px2sp(getContext(), txtBelowSize));
            mTxtTxtBelow.setTextColor(txtBelowColor);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = padding;
            mTxtTxtBelow.setLayoutParams(layoutParams);
        }
    }
}
