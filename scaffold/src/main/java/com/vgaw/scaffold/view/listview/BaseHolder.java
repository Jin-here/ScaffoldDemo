package com.vgaw.scaffold.view.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;

/**
 * @author caojin
 * @date 2017/12/11
 */

public abstract class BaseHolder {
    protected Context mContext;
    protected View mView;

    protected void init(Context context) {
        this.mContext = context;
        int layoutId = getLayoutId();
        if (layoutId != -1) {
            mView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        }
    }

    protected  @LayoutRes int getLayoutId() {
        return -1;
    }
}
