package com.vgaw.scaffold.util.cache;

import android.os.Parcelable;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;
import com.vgaw.scaffold.json.JsonUtil;

import java.lang.reflect.Type;
import java.util.Set;

public class KVFileCache {
    private static final String DEFAULT_NAME = "main";
    private MMKV mKv;

    public KVFileCache() {
        this(DEFAULT_NAME);
    }

    public KVFileCache(String name) {
        this(name, false);
    }

    public KVFileCache(String name, boolean multiProcess) {
        int mode = (multiProcess ? MMKV.MULTI_PROCESS_MODE : MMKV.SINGLE_PROCESS_MODE);
        mKv = MMKV.mmkvWithID(name, mode);
    }

    public <T> T getJson(String key, Class<T> cls) {
        if (!TextUtils.isEmpty(key)) {
            return JsonUtil.fromJson(getInstance().decodeString(key, null), cls);
        }
        return null;
    }

    public <T> T getJson(String key, Type type) {
        if (!TextUtils.isEmpty(key)) {
            return JsonUtil.fromJson(getInstance().decodeString(key, null), type);
        }
        return null;
    }

    public void setJson(String key, Object o) {
        if (!TextUtils.isEmpty(key) && o != null) {
            getInstance().encode(key, JsonUtil.toJson(o));
        }
    }

    public void setParcelable(String key, Parcelable value) {
        getInstance().encode(key, value);
    }

    public void set(String key, String value) {
        getInstance().encode(key, value);
    }

    public void setSet(String key, Set<String> value) {
        getInstance().encode(key, value);
    }

    public void set(String key, boolean value) {
        getInstance().encode(key, value);
    }

    public void setByteArray(String key, byte[] value) {
        getInstance().encode(key, value);
    }

    public void set(String key, int value) {
        getInstance().encode(key, value);
    }

    public void set(String key, long value) {
        getInstance().encode(key, value);
    }

    public void set(String key, float value) {
        getInstance().encode(key, value);
    }

    public void set(String key, double value) {
        getInstance().encode(key, value);
    }

    public <T extends Parcelable> T getParcelable(String key, Class<T> cls, T defaultValue) {
        return getInstance().decodeParcelable(key, cls, defaultValue);
    }

    public String get(String key, String defaultValue) {
        return getInstance().decodeString(key, defaultValue);
    }

    public Set<String> getSet(String key, Set<String> defaultValue) {
        return getInstance().decodeStringSet(key, defaultValue);
    }

    public boolean get(String key, boolean defaultValue) {
        return getInstance().decodeBool(key, defaultValue);
    }

    public byte[] getByteArray(String key, byte[] defaultValue) {
        return getInstance().decodeBytes(key, defaultValue);
    }

    public int get(String key, int defaultValue) {
        return getInstance().decodeInt(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return getInstance().decodeLong(key, defaultValue);
    }

    public float get(String key, float defaultValue) {
        return getInstance().decodeFloat(key, defaultValue);
    }

    public double get(String key, double defaultValue) {
        return getInstance().decodeDouble(key, defaultValue);
    }

    public String[] keys() {
        return getInstance().allKeys();
    }

    public void remove(String key) {
        getInstance().removeValueForKey(key);
    }

    public boolean contains(String key) {
        return getInstance().containsKey(key);
    }

    private MMKV getInstance() {
        return mKv;
    }
}