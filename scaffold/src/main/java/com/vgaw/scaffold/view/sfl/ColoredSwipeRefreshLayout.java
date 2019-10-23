package com.vgaw.scaffold.view.sfl;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author caojin
 * @date 2018/6/5
 */
public class ColoredSwipeRefreshLayout extends SwipeRefreshLayout {
    public ColoredSwipeRefreshLayout(@NonNull Context context) {
        super(context);
        applySetting();
    }

    public ColoredSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applySetting();
    }

    private void applySetting() {
        setColorSchemeResources(android.R.color.black,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
    }
}
