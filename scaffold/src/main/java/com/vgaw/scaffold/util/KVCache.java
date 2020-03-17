package com.vgaw.scaffold.util;

import android.os.Parcelable;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;
import com.vgaw.scaffold.json.JsonUtil;

import java.lang.reflect.Type;
import java.util.Set;

public class KVCache {
    public static <T> T get(String key, Class<T> cls) {
        if (!TextUtils.isEmpty(key)) {
            return JsonUtil.fromJson(getInstance().decodeString(key, null), cls);
        }
        return null;
    }

    public static <T> T get(String key, Type type) {
        if (!TextUtils.isEmpty(key)) {
            return JsonUtil.fromJson(getInstance().decodeString(key, null), type);
        }
        return null;
    }

    public static void set(String key, Object o) {
        if (!TextUtils.isEmpty(key) && o != null) {
            getInstance().encode(key, JsonUtil.toJson(o));
        }
    }

    public static void set(String key, Parcelable value) {
        getInstance().encode(key, value);
    }

    public static void set(String key, String value) {
        getInstance().encode(key, value);
    }

    public static void set(String key, Set<String> value) {
        getInstance().encode(key, value);
    }

    public static void set(String key, boolean value) {
        getInstance().encode(key, value);
    }

    public static void set(String key, byte[] value) {
        getInstance().encode(key, value);
    }

    public static void set(String key, int value) {
        getInstance().encode(key, value);
    }

    public static void set(String key, long value) {
        getInstance().encode(key, value);
    }

    public static void set(String key, float value) {
        getInstance().encode(key, value);
    }

    public static void set(String key, double value) {
        getInstance().encode(key, value);
    }

    public static <T extends Parcelable> T get(String key, Class<T> cls, T defaultValue) {
        return getInstance().decodeParcelable(key, cls, defaultValue);
    }

    public static String get(String key, String defaultValue) {
        return getInstance().decodeString(key, defaultValue);
    }

    public static Set<String> get(String key, Set<String> defaultValue) {
        return getInstance().decodeStringSet(key, defaultValue);
    }

    public static boolean get(String key, boolean defaultValue) {
        return getInstance().decodeBool(key, defaultValue);
    }

    public static byte[] get(String key, byte[] defaultValue) {
        return getInstance().decodeBytes(key, defaultValue);
    }

    public static int get(String key, int defaultValue) {
        return getInstance().decodeInt(key, defaultValue);
    }

    public static long get(String key, long defaultValue) {
        return getInstance().decodeLong(key, defaultValue);
    }

    public static float get(String key, float defaultValue) {
        return getInstance().decodeFloat(key, defaultValue);
    }

    public static double get(String key, double defaultValue) {
        return getInstance().decodeDouble(key, defaultValue);
    }

    public static void remove(String key) {
        getInstance().removeValueForKey(key);
    }

    public static boolean contains(String key) {
        return getInstance().containsKey(key);
    }

    private static MMKV getInstance() {
        return getInstance(false);
    }

    /**
     * you can override it when need, for example if you need isolated storage
     * @return
     */
    protected static MMKV getInstance(boolean multiProcess) {
        int mode = (multiProcess ? MMKV.MULTI_PROCESS_MODE : MMKV.SINGLE_PROCESS_MODE);
        return MMKV.mmkvWithID("main", mode);
    }
}