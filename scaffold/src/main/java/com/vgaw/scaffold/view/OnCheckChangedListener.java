package com.vgaw.scaffold.view;

public interface OnCheckChangedListener {
    /**
     * @param checked true：check变动交给开发者自己处理，否则相反;
     * @return
     */
    boolean onCheckChanged(boolean checked);
}
