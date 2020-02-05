package com.vgaw.scaffold.util;

import static com.vgaw.scaffold.util.Util.getDot;

public class DistanceUtil {
    public static String formatLength(int length){
        if (length < 1000){
            return length + "m";
        }else {
            return getDot(((float) length / 1000), 2) + "km";
        }
    }

    public static float metric2Imperial(float raw) {
        return raw * 3.2808F;
    }

    /**
     * @param raw m/s
     * @param type 0: MPH; 1: km/h; 2: m/s
     * @return
     */
    public static float getSpeed(float raw, int type) {
        switch (type) {
            case 0:
                return raw * 2.23693629f;
            case 1:
                return raw * 3.6f;
            default:
                return raw;
        }
    }
}
