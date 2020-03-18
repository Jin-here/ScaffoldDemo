package com.vgaw.scaffold.view.tab;

import android.content.Context;
import android.view.View;

import com.vgaw.scaffold.util.Util;
import com.vgaw.scaffold.view.checkable.CheckableAdapter;

public class DefaultTabAdapter extends CheckableAdapter<String> {
    private DefaultTabStyle mStyle;

    public DefaultTabAdapter(Context context, String[] dataArray, DefaultTabStyle style) {
        super(context, dataArray);
        mStyle = style;
    }

    @Override
    public View createView(int position) {
        DefaultTabView view = new DefaultTabView(mContext);
        view.setTabStyle(mStyle);
        view.setText(Util.nullToEmpty(getItem(position)));
        return view;
    }
}
