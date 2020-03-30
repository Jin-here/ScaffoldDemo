package com.vgaw.scaffold.http.callback;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.http.ErrorMap;
import com.vgaw.scaffold.util.context.ContextManager;
import com.vgaw.scaffold.view.AppToast;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author caojin
 * @date 2018/6/9
 */
public abstract class HttpResultCallback<T> implements Callback<T>, HttpResultInterface<T> {
    @Override
    public void onFinished() {}

    @Override
    public void onFail(String msg) {
        AppToast.show(msg);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            if (response.isSuccessful()) {
                T responseBody = response.body();
                if (responseBody != null) {
                    onSuccess(responseBody);
                }
            } else {
                ResponseBody errorBody = response.errorBody();
                if (errorBody != null) {
                    onFail(errorBody.string());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        onFinished();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {
            onFail(ContextManager.getInstance().getString(R.string.base_http_time_out));
        } else {
            onFail(ErrorMap.getUnknownMsg());
        }
    }
}
