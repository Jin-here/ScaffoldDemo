package com.vgaw.scaffolddemo.data.auth;

import com.vgaw.scaffolddemo.data.user.UserInfoCache;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * @author caojin
 * @date 2018/6/6
 */
public class AuthRepository implements AuthDataSource {
    private static volatile AuthRepository sInstance = null;

    private AuthRepository() {}

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
    public Observable<ResponseBody> login(String account, String password) {
        afterLogin(null);
        return null;
    }

    private void afterLogin(AuthInfo authInfo) {
        UserInfoCache.getInstance().setAuthInfo(authInfo);
    }
}
