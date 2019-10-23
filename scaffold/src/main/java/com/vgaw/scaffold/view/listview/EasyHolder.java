package com.vgaw.scaffold.view.listview;

import android.view.View;

import java.util.List;

public abstract class EasyHolder<T> extends BaseHolder {
    protected abstract View createView(int position);

    protected abstract void  refreshView(int position, T item);

    protected void onGetExtraData(List<Object> extraDataList) {}
}
