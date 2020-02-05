package com.vgaw.scaffold.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import java.util.regex.Pattern;

/**
 * Created by caojin on 2016/8/15.
 */
public class Util {
    public static boolean equalNonNull(Object one, Object other) {
        return (one != null && other != null && one.equals(other));
    }

    public static boolean equal(Object one, Object other) {
        return ((one == null && other == null) || (one != null && other != null && one.equals(other)));
    }

    public static boolean nullTo(Integer raw, boolean other) {
        return raw == null ? other : (raw == 1);
    }

    public static int nullTo(Integer raw, int other) {
        return raw == null ? other : raw;
    }

    public static float nullTo(Float raw, float other) {
        return raw == null ? other : raw;
    }

    public static float nullTo(Integer raw, float other) {
        return raw == null ? other : raw;
    }

    public static String nullToEmpty(String raw) {
        return raw == null ? "" : raw;
    }

    public static String nullTo(String raw, String after) {
        return raw == null ? after : raw;
    }

    public static String emptyTo(String raw, String after){
        return "".equals(raw) ? after : raw;
    }

    public static String addZero(int raw){
        return raw > 9 ? String.valueOf(raw) : ("0" + raw);
    }

    public static String getDot(float raw, int limit){
        return String.format("%." + limit + "f", raw);
    }

    public static boolean isInt(float raw) {
        int temp = (int) raw;
        return (temp == raw);
    }

    public static Long getTrafficNow() {
        Long rx = TrafficStats.getUidRxBytes(android.os.Process.myUid());
        Long tx = TrafficStats.getUidTxBytes(android.os.Process.myUid());
        if (rx == TrafficStats.UNSUPPORTED || tx == TrafficStats.UNSUPPORTED) {
            return null;
        }
        return rx + tx;
    }

    public static String formatTraffic(Long raw) {
        if (raw == null) {
            return "UNSUPPORTED";
        }
        if (raw < 1024 * 1024) {
            return (raw / 1024) + "KB";
        }
        if (raw < 1024 * 1024 * 1024) {
            return (raw / 1024 / 1024) + "MB";
        }
        return (raw / 1024 / 1024 / 1024) + "GB";
    }

    public static boolean passwordSimple(String raw) {
        String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z._~!? -]{6,18}$";
        return !Pattern.compile(pattern).matcher(raw).matches();
    }

    public static boolean phoneValid(String raw) {
        String pattern = "^1[0-9]{10}$";
        return Pattern.compile(pattern).matcher(raw).matches();
    }

    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getPackageInfo(context.getPackageName(), 0);
    }
}
