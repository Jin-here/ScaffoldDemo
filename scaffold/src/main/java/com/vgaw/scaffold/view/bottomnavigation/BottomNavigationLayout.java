package com.vgaw.scaffold.view.bottomnavigation;

import android.content.Context;
import android.util.AttributeSet;

import com.vgaw.scaffold.util.phone.DensityUtil;
import com.vgaw.scaffold.view.checkable.CheckableLayout;
import com.vgaw.scaffold.view.msgtip.MsgTipItemInterface;
import com.vgaw.scaffold.view.msgtip.MsgTipLayoutInterface;

public class BottomNavigationLayout extends CheckableLayout implements MsgTipLayoutInterface {
    public BottomNavigationLayout(Context context) {
        super(context);
        init();
    }

    public BottomNavigationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomNavigationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setMsgTip(int index, int unReadCount) {
        ((MsgTipItemInterface) getChildAt(index)).onMsgNumChanged(unReadCount);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(DensityUtil.dp2px(getContext(), 48), MeasureSpec.EXACTLY));
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }
}
