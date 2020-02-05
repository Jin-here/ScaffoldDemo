package com.vgaw.scaffolddemo.http;

import android.view.View;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by caojin on 2017/6/23.
 */

public abstract class StringCallback<T> extends BaseCallback {
    private View mActionView;

    public StringCallback() {}

    public StringCallback(View actionView) {
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
                    onSuccess(body);
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

    protected abstract void onSuccess(String response);

    private void onFinished() {
        if (mActionView != null) {
            mActionView.setEnabled(true);
        }
    }
}

