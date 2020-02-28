package com.vgaw.scaffold.util;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.vgaw.scaffold.json.JsonUtil;
import com.vgaw.scaffold.util.context.ContextManager;

import java.lang.reflect.Type;

public class PreferenceUtil {
    private static SharedPreferences getDefault() {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextManager.getInstance().getApplicationContext());
    }

    public static void clear(String key) {
        if (!TextUtils.isEmpty(key)) {
            getDefault().edit()
                    .remove(key)
                    .commit();
        }
    }

    public static <T> T get(String key, Class<T> cls) {
        if (!TextUtils.isEmpty(key)) {
            return JsonUtil.fromJson(getDefault().getString(key, null), cls);
        }
        return null;
    }

    public static <T> T get(String key, Type type) {
        if (!TextUtils.isEmpty(key)) {
            return JsonUtil.fromJson(getDefault().getString(key, null), type);
        }
        return null;
    }

    public static void set(String key, Object o) {
        if (!TextUtils.isEmpty(key) && o != null) {
            getDefault().edit()
                    .putString(key, JsonUtil.toJson(o))
                    .commit();
        }
    }

    public static void setAsync(String key, Object o) {
        if (!TextUtils.isEmpty(key) && o != null) {
            getDefault().edit()
                    .putString(key, JsonUtil.toJson(o))
                    .apply();
        }
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static float getFloat(String key) {
        return getFloat(key, 0);
    }

    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        if (!TextUtils.isEmpty(key)) {
            return getDefault().getBoolean(key, defValue);
        }
        return false;
    }

    public static float getFloat(String key, float defValue) {
        if (!TextUtils.isEmpty(key)) {
            return getDefault().getFloat(key, defValue);
        }
        return 0;
    }

    public static long getLong(String key, long defValue) {
        if (!TextUtils.isEmpty(key)) {
            return getDefault().getLong(key, defValue);
        }
        return 0;
    }

    /**
     * don't use {@link #set(String, Object)} when you can use this method as to save space
     * @param key
     * @param value
     */
    public static void setBoolean(String key, boolean value) {
        if (!TextUtils.isEmpty(key)) {
            getDefault().edit()
                    .putBoolean(key, value)
                    .commit();
        }
    }

    /**
     * don't use {@link #set(String, Object)} when you can use this method as to save space
     * @param key
     * @param value
     */
    public static void setFloat(String key, float value) {
        if (!TextUtils.isEmpty(key)) {
            getDefault().edit()
                    .putFloat(key, value)
                    .commit();
        }
    }

    /**
     * don't use {@link #set(String, Object)} when you can use this method as to save space
     * @param key
     * @param value
     */
    public static void setLong(String key, long value) {
        if (!TextUtils.isEmpty(key)) {
            getDefault().edit()
                    .putLong(key, value)
                    .commit();
        }
    }

    /**
     * don't use {@link #set(String, Object)} when you can use this method as to save space
     * @param key
     * @param value
     */
    public static void setBooleanAsync(String key, boolean value) {
        if (!TextUtils.isEmpty(key)) {
            getDefault().edit()
                    .putBoolean(key, value)
                    .apply();
        }
    }

    /**
     * don't use {@link #set(String, Object)} when you can use this method as to save space
     * @param key
     * @param value
     */
    public static void setFloatAsync(String key, float value) {
        if (!TextUtils.isEmpty(key)) {
            getDefault().edit()
                    .putFloat(key, value)
                    .apply();
        }
    }

    /**
     * don't use {@link #set(String, Object)} when you can use this method as to save space
     * @param key
     * @param value
     */
    public static void setLongAsync(String key, long value) {
        if (!TextUtils.isEmpty(key)) {
            getDefault().edit()
                    .putLong(key, value)
                    .apply();
        }
    }
}