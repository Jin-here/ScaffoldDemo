package com.vgaw.scaffolddemo.http.rx;

import android.text.TextUtils;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.vgaw.scaffold.json.JsonUtil;
import com.vgaw.scaffold.view.AppToast;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

public abstract class HttpResultObserver<T> extends DisposableObserver<ResponseBody> implements HttpResultInterface<T> {
    @Override
    public void onFail(String msg) {
        AppToast.show(msg);
    }

    @Override
    public void onFinished() {}

    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            if (responseBody != null) {
                String body = responseBody.string();
                if (!TextUtils.isEmpty(body)) {
                    Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    T result = null;
                    if (type instanceof Class) {
                        result = JsonUtil.fromJson(body, type);
                    } else if (type instanceof ParameterizedType) {
                        ParameterizedTypeImpl parameterizedType = new ParameterizedTypeImpl(((ParameterizedType) type).getActualTypeArguments(), ((ParameterizedType) type).getOwnerType(), ((ParameterizedType) type).getRawType());
                        result = JsonUtil.fromJson(body, parameterizedType);
                    }
                    if (result != null) {
                        onSuccess(result);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        onFinished();
    }

    @Override
    public void onError(Throwable e) {
        onFail(e.getLocalizedMessage());
        onFinished();
    }

    @Override
    public void onComplete() {
        onFinished();
    }
}
