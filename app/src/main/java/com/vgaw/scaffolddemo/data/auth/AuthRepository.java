package com.vgaw.scaffolddemo.data.auth;

import com.vgaw.scaffolddemo.data.user.UserInfoCache;
import com.vgaw.scaffold.http.bean.HttpResult;
import com.vgaw.scaffold.http.callback.HttpResultCallback;
import com.vgaw.scaffolddemo.model.AuthInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @author caojin
 * @date 2018/6/6
 */
public class AuthRepository implements AuthDataSource {
    private static volatile AuthRepository sInstance = null;

    private AuthRepository() {
    }

    public static AuthRepository getInstance() {
        if (sInstance == null) {
            synchronized (AuthRepository.class) {
                if (sInstance == null) {
                    sInstance = new AuthRepository();
                }
            }
        }
        return sInstance;
    }


    @Override
    public Call<ResponseBody> login(String account, String password, HttpResultCallback<HttpResult<AuthInfo>> callback) {
        afterLogin(null);
        return null;
    }

    private void afterLogin(AuthInfo authInfo) {
        UserInfoCache.getInstance().setAuthInfo(authInfo);
    }
}
