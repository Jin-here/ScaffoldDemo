package com.vgaw.scaffold.view.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import com.vgaw.scaffold.util.phone.DensityUtil;
import com.vgaw.scaffold.view.checkable.CheckableItemInterface;
import com.vgaw.scaffold.view.msgtip.MsgTipItemInterface;

public class DefaultTabView extends AppCompatTextView implements CheckableItemInterface, MsgTipItemInterface {
    private DefaultTabStyle mStyle;

    public DefaultTabView(Context context) {
        super(context);
        init();
    }

    public DefaultTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DefaultTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTabStyle(DefaultTabStyle tabStyle) {
        mStyle = tabStyle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = DensityUtil.dp2px(getContext(), 64);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    private void init() {
        int padding = DensityUtil.dp2px(getContext(), 6);
        setPadding(padding, 0, padding, 0);
        setGravity(Gravity.CENTER);
    }

    @Override
    public void onCheckChanged(boolean checkedNow) {
        setTextColor(getContext().getResources().getColor(mStyle.getTxtColor(checkedNow)));
        setTextSize(mStyle.getTxtSize(checkedNow));
    }

    @Override
    public void onMsgNumChanged(int msgCountNow) {}
}
