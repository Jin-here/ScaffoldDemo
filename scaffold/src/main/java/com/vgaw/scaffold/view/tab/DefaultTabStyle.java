package com.vgaw.scaffold.view.tab;

public class DefaultTabStyle {
    private int txtColor;
    private int txtColorChecked;
    private int txtSize;
    private int txtSizeChecked;

    public DefaultTabStyle(int txtColor, int txtColorChecked, int txtSize, int txtSizeChecked) {
        this.txtColor = txtColor;
        this.txtColorChecked = txtColorChecked;
        this.txtSize = txtSize;
        this.txtSizeChecked = txtSizeChecked;
    }

    public int getTxtColor(boolean checked) {
        return (checked ? txtColorChecked : txtColor);
    }

    public int getTxtSize(boolean checked) {
        return (checked ? txtSizeChecked : txtSize);
    }
}
