package com.vgaw.scaffolddemo.data.auth;

import com.vgaw.scaffolddemo.http.bean.HttpResult;
import com.vgaw.scaffolddemo.http.callback.HttpResultCallback;

import okhttp3.ResponseBody;
import retrofit2.Call;

public interface AuthDataSource {
    Call<ResponseBody> login(String account, String password, HttpResultCallback<HttpResult<AuthInfo>> callback);
}
