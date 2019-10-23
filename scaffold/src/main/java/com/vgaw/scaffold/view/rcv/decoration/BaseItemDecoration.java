package com.vgaw.scaffold.view.rcv.decoration;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseItemDecoration extends RecyclerView.ItemDecoration {
    private int mHeaderCount;

    public void setHeaderCount(int headerCount) {
        mHeaderCount = headerCount;
    }

    protected boolean headerOrFooter(int itemPosition, int itemCount) {
        return (itemPosition < mHeaderCount || (itemPosition > itemCount + mHeaderCount - 1));
    }

    protected int getActualItmePosition(int rawItemPosition) {
        return rawItemPosition - mHeaderCount;
    }
}
