package com.vgaw.scaffolddemo.http.callback;

import android.text.TextUtils;

import com.vgaw.scaffold.json.JsonUtil;
import com.vgaw.scaffold.json.typeimpl.ParameterizedTypeImpl;
import com.vgaw.scaffold.util.context.ContextManager;
import com.vgaw.scaffold.view.AppToast;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.http.ErrorMap;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author caojin
 * @date 2018/6/9
 */
public abstract class HttpResultCallback<T> implements Callback<ResponseBody>, HttpResultInterface<T> {
    @Override
    public void onFinished() {}

    @Override
    public void onFail(String msg) {
        AppToast.show(msg);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                if (response.isSuccessful()) {
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
                            onFinished();
                            return;
                        }
                    }
                }
            }

            ResponseBody errorBody = response.errorBody();
            if (errorBody != null) {
                onFail(errorBody.string());
                onFinished();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {
            onFail(ContextManager.getInstance().getString(R.string.base_http_time_out));
        } else {
            onFail(ErrorMap.getUnknownMsg());
        }
    }
}
