package com.vgaw.scaffolddemo.data.auth;

import com.vgaw.scaffold.http.bean.HttpResult;
import com.vgaw.scaffold.http.callback.HttpResultCallback;
import com.vgaw.scaffolddemo.model.AuthInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;

public interface AuthDataSource {
    Call<ResponseBody> login(String account, String password, HttpResultCallback<HttpResult<AuthInfo>> callback);
}
