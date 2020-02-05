package com.vgaw.scaffolddemo.http;

import com.vgaw.scaffold.util.context.ContextManager;
import com.vgaw.scaffolddemo.R;

import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author caojin
 * @date 2018/6/9
 */
public abstract class BaseCallback implements Callback<ResponseBody> {
    protected static final int ERROR_UNKNOWN = -1;

    protected String mapError(int code) {
        if (code == ERROR_UNKNOWN) {
            return ContextManager.getInstance().getString(R.string.base_unknown);
        } else if (code > 499 && code < 600) {
            return ContextManager.getInstance().getString(R.string.base_http_server_error);
        }
        return ErrorMap.getErrorMsg(code);
    }

    protected abstract void onFail(int code, String msg);

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {
            onFail(ERROR_UNKNOWN, ContextManager.getInstance().getString(R.string.base_http_time_out));
        } else {
            onFail(ERROR_UNKNOWN, mapError(ERROR_UNKNOWN));
        }
    }
}
