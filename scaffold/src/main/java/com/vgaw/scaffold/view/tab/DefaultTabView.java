package com.vgaw.scaffold.view.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.view.checkable.CheckableItemInterface;
import com.vgaw.scaffold.view.msgtip.MsgTipItemInterface;

public class DefaultTabView extends FrameLayout implements CheckableItemInterface, MsgTipItemInterface {
    private TextView mDefaultTabItemContent;
    private TextView mDefaultTabItemBubble;

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

    public void setContent(String content) {
        mDefaultTabItemContent.setText(content);
    }

    public void setTabStyle(DefaultTabStyle tabStyle) {
        mStyle = tabStyle;
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.default_tab_item, this);
        mDefaultTabItemContent = view.findViewById(R.id.default_tab_item_content);
        mDefaultTabItemBubble = view.findViewById(R.id.default_tab_item_bubble);
    }

    @Override
    public void onCheckChanged(boolean checkedNow) {
        mDefaultTabItemContent.setTextColor(getContext().getResources().getColor(mStyle.getTxtColor(checkedNow)));
        mDefaultTabItemContent.setTextSize(mStyle.getTxtSize(checkedNow));
    }

    @Override
    public void onMsgNumChanged(int msgCountNow) {
        if (msgCountNow < 1) {
            mDefaultTabItemBubble.setVisibility(GONE);
        } else {
            String s;
            if (msgCountNow < 100) {
                s = String.valueOf(msgCountNow);
            } else {
                s = "99+";
            }
            mDefaultTabItemBubble.setText(s);
            mDefaultTabItemBubble.setVisibility(VISIBLE);
        }
    }
}
