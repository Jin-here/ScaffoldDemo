package com.vgaw.scaffold.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
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
}
