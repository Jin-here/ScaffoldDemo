package com.vgaw.scaffolddemo.http;

import android.view.View;

import com.vgaw.scaffold.json.JsonUtil;
import com.vgaw.scaffold.json.TypeBuilder;
import com.vgaw.scaffolddemo.http.bean.BaseListBean;
import com.vgaw.scaffolddemo.http.bean.ResponseOverview;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by caojin on 2017/6/23.
 */

public abstract class JsonCallback<T> extends BaseCallback {
    private View mActionView;

    public JsonCallback() {}

    public JsonCallback(View actionView) {
        this.mActionView = actionView;
        this.mActionView.setEnabled(false);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String body = responseBody.string();
                if (response.isSuccessful()) {
                    ResponseOverview responseOverview = JsonUtil.fromJson(body, ResponseOverview.class);
                    if (responseOverview != null) {
                        String body1 = responseOverview.getBody();
                        Integer code = responseOverview.getCode();
                        if (code != null) {
                            if (code == 0) {
                                Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                                if (type instanceof Class) {
                                    T result = (T) JsonUtil.fromJson(body1, type);
                                    if (result != null) {
                                        onSuccess(result);
                                    }
                                } else if (type instanceof ParameterizedType) {
                                    String aClass = ((ParameterizedType) type).getRawType().toString();
                                    T result = null;
                                    if (body1 == null) {
                                        onSuccess(null);
                                        return;
                                    }
                                    if ("interface java.util.List".equals(aClass)) {
                                        result = (T) JsonUtil.fromJson(body1, TypeBuilder.newInstance(List.class)
                                                .addTypeParam(((ParameterizedType) type).getActualTypeArguments()[0])
                                                .build());
                                    } else {
                                        result = (T) JsonUtil.fromJson(body1, TypeBuilder.newInstance(BaseListBean.class)
                                                .addTypeParam(((ParameterizedType) type).getActualTypeArguments()[0])
                                                .build());
                                    }
                                    if (result != null) {
                                        onSuccess(result);
                                    }
                                }
                                return;
                            } else {
                                onFail(code, mapError(code));
                                return;
                            }
                        }
                    }
                }
            }

            ResponseBody errorBody = response.errorBody();
            if (errorBody != null) {
                String error = errorBody.string();
                onFail(response.code(), error);
            }
        } catch (IOException e) {
            e.printStackTrace();
            onFail(ERROR_UNKNOWN, mapError(ERROR_UNKNOWN));
        }
        onFinished();
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        super.onFailure(call, t);
        onFinished();
    }

    protected abstract void onSuccess(T response);

    private void onFinished() {
        if (mActionView != null) {
            mActionView.setEnabled(true);
        }
    }
}

