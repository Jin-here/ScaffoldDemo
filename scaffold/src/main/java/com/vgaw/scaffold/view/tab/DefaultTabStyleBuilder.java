package com.vgaw.scaffold.view.tab;

import androidx.annotation.ColorRes;

public class DefaultTabStyleBuilder {
    private int mSizeNormal;
    private int mSizeChecked;
    private int mColorNormal;
    private int mColorChecked;

    public DefaultTabStyleBuilder txtColor(@ColorRes int colorNormal, @ColorRes int colorChecked) {
        mColorNormal = colorNormal;
        mColorChecked = colorChecked;
        return this;
    }

    /**
     * unit is sp
     * @param sizeNormal
     * @param sizeChecked
     * @return
     */
    public DefaultTabStyleBuilder txtSize(int sizeNormal, int sizeChecked) {
        mSizeNormal = sizeNormal;
        mSizeChecked = sizeChecked;
        return this;
    }

    public DefaultTabStyle build() {
        return new DefaultTabStyle(mColorNormal, mColorChecked, mSizeNormal, mSizeChecked);
    }
}
