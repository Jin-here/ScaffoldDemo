package com.vgaw.scaffold.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class NoScrollbarScrollView extends ScrollView {
    public NoScrollbarScrollView(Context context) {
        super(context);
        init();
    }

    public NoScrollbarScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoScrollbarScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
    }
}
