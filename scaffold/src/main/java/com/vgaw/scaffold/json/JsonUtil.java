package com.vgaw.scaffold.json;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vgaw.scaffold.json.callback.IterationCallback;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by caojin on 2017/5/24.
 * 其中TypeBuilder相关类来自
 * https://github.com/ikidou/TypeBuilder, 提交位置(f49c965: "update .gitignore")
 */

public class JsonUtil {
    private static final String TAG = "JsonUtil";

    public static String toJson(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * @param json
     * @param cls     普通类(泛型类请使用{@link #fromJson(String, Type)})
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> cls) {
        T t = null;
        try {
            t = JSON.parseObject(json, cls);
        } catch (Exception e) {
            Log.e(TAG, "fromJson: " + e.getLocalizedMessage());
        }
        return t;
    }

    /**
     * type构造方法请参考:
     * https://github.com/ikidou/TypeBuilder
     * @param json
     * @param type    泛型类(普通类请使用{@link #fromJson(String, Class)})
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Type type) {
        T t = null;
        try {
            t = JSON.parseObject(json, type);
        } catch (Exception e) {
            Log.e(TAG, "fromJson: " + e.getLocalizedMessage());
        }
        return t;
    }

    public static void iterate(String json, IterationCallback callback) {
        JSONObject jsonObject = JSON.parseObject(json);
        Iterator<Map.Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            callback.onIterate(next.getKey(), next.getValue().toString());
        }
    }
}
