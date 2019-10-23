package com.vgaw.scaffolddemo.http;

import com.vgaw.scaffold.util.context.ContextManager;
import com.vgaw.scaffolddemo.R;

import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * @author caojin
 * @date 2018/6/9
 */
public abstract class BaseCallback implements Callback<ResponseBody> {
    protected static final int ERROR_UNKNOWN = -1;

    protected void onError(String error) {}

    public static String mapError(int code) {
        if (code == ERROR_UNKNOWN) {
            return ContextManager.getInstance().getString(R.string.base_unknown);
        } else if (code > 499 && code < 600) {
            return ContextManager.getInstance().getString(R.string.base_http_server_error);
        }
        return null;
    }
}
