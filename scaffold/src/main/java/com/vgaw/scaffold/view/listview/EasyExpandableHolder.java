package com.vgaw.scaffold.view.listview;

import android.view.View;

/**
 * @author caojin
 * @date 2017/12/11
 */

public abstract class EasyExpandableHolder extends BaseHolder {
    protected abstract View createView(int groupPosition, int childPosition);

    protected abstract void refreshView(int groupPosition, int childPosition, Object item);
}
