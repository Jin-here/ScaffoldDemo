package com.vgaw.scaffold.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by caojin on 2016/8/15.
 */
public class Util {
    public static float metric2Imperial(float raw) {
        return raw * 3.2808F;
    }

    public static boolean isInt(float raw) {
        int temp = (int) raw;
        return (temp == raw);
    }

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

    private static String addZero(int raw){
        return raw > 9 ? String.valueOf(raw) : ("0" + raw);
    }

    public static String getDot(float raw, int limit){
        return String.format("%." + limit + "f", raw);
    }

    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String formatSecond(String format, long second) {
        return formatMillisecond(format, second * 1000);
    }

    /**
     * @param format
     * @param millisecond 注意不要传int
     * @return
     */
    public static String formatMillisecond(String format, long millisecond) {
        return new SimpleDateFormat(format).format(new Date(millisecond));
    }

    public static String formatISO8601Time(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        // Quoted "Z" to indicate UTC, no timezone offset
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static boolean checkGpsCoordinate(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    public static String calLength(int length){
        if (length < 1000){
            return length + "m";
        }else {
            return getDot(((float) length / 1000), 2) + "km";
        }
    }

    public static float getFoot(float raw) {
        return raw * 3.280839895f;
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

    public static boolean isPasswordSimple(String raw) {
        String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z._~!? -]{6,18}$";
        return !Pattern.compile(pattern).matcher(raw).matches();
    }

    public static boolean isPhoneValid(String raw) {
        String pattern = "^1[0-9]{10}$";
        return Pattern.compile(pattern).matcher(raw).matches();
    }

    /**
     * 判断是否全是数字
     * @param str
     * @return
     */
    public static boolean isNumeric (String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getPackageInfo(context.getPackageName(), 0);
    }

    public static boolean anotherDay(int savedDay) {
        //获得保存的天数，如果没有记录就赋值为-1表示第一次执行
        int curDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        return (savedDay == -1 || savedDay != curDay);
    }

    public static boolean usernameValid(String raw) {
        String pattern = "^([a-zA-Z\\u4e00-\\u9fa5]{1}[\\u4e00-\\u9fa5a-zA-Z0-9_-]{1,13}[a-zA-Z0-9\\u4e00-\\u9fa5]{1})|([\\u4e00-\\u9fa5]{2})$";
        return Pattern.compile(pattern).matcher(raw).matches();
    }

    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;
    }

    public static boolean containChinese(String str, boolean all) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                if (!all) {
                    return true;
                }
            } else if (all) {
                return false;
            }
        }
        return all;
    }

    public static void putValue(String key, String value, Map<String, Object> map) {
        if (!TextUtils.isEmpty(value)) {
            map.put(key, value);
        }
    }

    public static boolean hex(char raw) {
        return raw > 47 && raw < 58 ||
                raw > 64 && raw < 71 ||
                raw > 96 && raw < 103;
    }

    public static int hex2ten(String hexStr) {
        return Integer.parseInt(hexStr,16);
    }

    public static String ten2Hex(int raw) {
        return Integer.toHexString(raw);
    }
}
