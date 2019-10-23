package com.vgaw.scaffolddemo.http;

import android.view.View;

import com.vgaw.scaffold.json.JsonUtil;
import com.vgaw.scaffold.json.TypeBuilder;
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
                    Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    if (type instanceof Class) {
                        onSuccess((T) JsonUtil.fromJson(body, type));
                    } else if (type instanceof ParameterizedType) {
                        ResponseOverview responseOverview = JsonUtil.fromJson(body, ResponseOverview.class);

                        onSuccess((T) JsonUtil.fromJson(responseOverview.getResults(), TypeBuilder.newInstance(List.class)
                                .addTypeParam(((ParameterizedType) type).getActualTypeArguments()[0])
                                .build()));
                    }
                } else {
                    ResponseOverview responseOverview = JsonUtil.fromJson(body, ResponseOverview.class);
                    String error = responseOverview.getError();
                    onError(error);
                }
            } else {
                ResponseBody errorBody = response.errorBody();
                if (errorBody != null) {
                    ResponseOverview responseOverview = JsonUtil.fromJson(errorBody.string(), ResponseOverview.class);
                    String error = responseOverview.getError();
                    onError(error);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            onError(BaseCallback.mapError(ERROR_UNKNOWN));
        }
        onFinished();
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        onError(BaseCallback.mapError(ERROR_UNKNOWN));
        onFinished();
    }

    public abstract void onSuccess(T response);

    public void onFinished() {
        if (mActionView != null) {
            mActionView.setEnabled(true);
        }
    }
}

