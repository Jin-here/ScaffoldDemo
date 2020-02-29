package com.vgaw.scaffolddemo.http.rx;

import android.text.TextUtils;

import com.vgaw.scaffold.json.JsonUtil;
import com.vgaw.scaffold.json.typeimpl.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class HttpResultFunction<R> implements Function<ResponseBody, R> {
    @Override
    public R apply(ResponseBody responseBody) throws Exception {
        R result = null;
        try {
            if (responseBody != null) {
                String body = responseBody.string();
                if (!TextUtils.isEmpty(body)) {
                    Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    if (type instanceof Class) {
                        result = JsonUtil.fromJson(body, type);
                    } else if (type instanceof ParameterizedType) {
                        ParameterizedTypeImpl parameterizedType = new ParameterizedTypeImpl(((ParameterizedType) type).getActualTypeArguments(), ((ParameterizedType) type).getOwnerType(), ((ParameterizedType) type).getRawType());
                        result = JsonUtil.fromJson(body, parameterizedType);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
