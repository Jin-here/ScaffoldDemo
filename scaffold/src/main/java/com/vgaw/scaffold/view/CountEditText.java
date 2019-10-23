package com.vgaw.scaffold.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.google.android.material.textfield.TextInputEditText;
import com.vgaw.scaffold.R;

public class CountEditText extends LinearLayout {
    private TextInputEditText mCountEdittextContent;
    private TextView mCountEdittextCount;

    private int mCountMaxLength;
    private String mCountHint;
    private NestedScrollView mScrollerView;

    public CountEditText(Context context) {
        super(context);
        init(null);
    }

    public CountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CountEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setScrollerView(NestedScrollView scrollerView) {
        mScrollerView = scrollerView;
    }

    public void setText(String content) {
        mCountEdittextContent.setText(content);
    }

    public String getText() {
        return mCountEdittextContent.getText().toString();
    }

    private void init(AttributeSet attrs) {
        setOrientation(VERTICAL);

        View view = View.inflate(getContext(), R.layout.count_edittext_layout, this);
        mCountEdittextContent = view.findViewById(R.id.count_edittext_content);
        mCountEdittextCount = view.findViewById(R.id.count_edittext_count);

        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CountEditText);
            mCountMaxLength = array.getInt(R.styleable.CountEditText_countMaxLength, 0);
            mCountHint = array.getString(R.styleable.CountEditText_countHint);

            array.recycle();
        }

        updateCount(mCountEdittextContent.getText().length());
        mCountEdittextContent.setHint(mCountHint);
        mCountEdittextContent.setMaxEms(mCountMaxLength);
        mCountEdittextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateCount(s.length());
            }
        });
        mCountEdittextContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 设置ScrollView不拦截事件
                if (mScrollerView != null) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            // 手指离开时：重置ScrollView事件拦截的状态
                            mScrollerView.requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            // 手指离开时：重置ScrollView事件拦截的状态
                            mScrollerView.requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void updateCount(int count) {
        mCountEdittextCount.setText(String.format("%d/%d", count, mCountMaxLength));
    }
}
