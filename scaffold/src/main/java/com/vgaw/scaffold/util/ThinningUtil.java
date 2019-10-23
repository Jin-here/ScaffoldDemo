package com.vgaw.scaffold.util;

/**
 * Created by caojin on 2017/9/16.
 *
 * 将请求抽稀
 */

public class ThinningUtil {
    private byte group;
    private byte count;

    /**
     * 每遇到group次，才触发一次
     * 第一次请求默认通过
     * @param group
     */
    public ThinningUtil(byte group) {
        this.group = group;
        this.count = this.group;
    }

    public boolean thin() {
        if (count == group) {
            count = 0;
            return true;
        }
        count++;
        return false;
    }
}
