package com.vgaw.scaffolddemo.data.user;

import com.vgaw.scaffold.util.KVCache;
import com.vgaw.scaffolddemo.model.AuthInfo;
import com.vgaw.scaffolddemo.model.UserInfo;

public class UserInfoCache {
    private static volatile UserInfoCache sInstance;

    private UserInfo mUserInfo;
    private AuthInfo mAuthInfo;

    private UserInfoCache() {}

    public static UserInfoCache getInstance() {
        if (sInstance == null) {
            synchronized (UserInfoCache.class) {
                if (sInstance == null) {
                    sInstance = new UserInfoCache();
                }
            }
        }
        return sInstance;
    }

    public UserInfo getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = KVCache.get("user_info", UserInfo.class);
        }
        return mUserInfo;
    }

    public AuthInfo getAuthInfo() {
        if (mAuthInfo == null) {
            mAuthInfo = KVCache.get("auth_info", AuthInfo.class);
        }
        return mAuthInfo;
    }

    public void setUserInfo(UserInfo info) {
        if (info != null) {
            mUserInfo = info;
            KVCache.set("user_info", info);
        }
    }

    public void setAuthInfo(AuthInfo info) {
        if (info != null) {
            mAuthInfo = info;
            KVCache.set("auth_info", info);
        }
    }

    public String getId() {
        UserInfo userInfo = getUserInfo();
        if (userInfo != null) {
            return userInfo.getId();
        }
        return null;
    }

    public String getToken() {
        AuthInfo authInfo = getAuthInfo();
        if (authInfo != null) {
            return authInfo.getToken();
        }
        return null;
    }

    public String getName() {
        UserInfo userInfo = getUserInfo();
        if (userInfo != null) {
            return userInfo.getName();
        }
        return null;
    }

    public String getAvatar() {
        UserInfo userInfo = getUserInfo();
        if (userInfo != null) {
            return userInfo.getAvatar();
        }
        return null;
    }
}
