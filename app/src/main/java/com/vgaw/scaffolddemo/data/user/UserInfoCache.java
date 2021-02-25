package com.vgaw.scaffolddemo.data.user;

import com.vgaw.scaffold.util.cache.KVFileCache;
import com.vgaw.scaffolddemo.model.AuthInfo;
import com.vgaw.scaffolddemo.model.UserInfo;

public class UserInfoCache {
    private static final String NAME = "UserInfoCache";
    private static volatile UserInfoCache sInstance;

    private UserInfo mUserInfo;
    private AuthInfo mAuthInfo;
    
    private KVFileCache mCache;

    private UserInfoCache() {
        mCache = new KVFileCache(NAME);
    }

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
            mUserInfo = mCache.getJson("user_info", UserInfo.class);
        }
        return mUserInfo;
    }

    public AuthInfo getAuthInfo() {
        if (mAuthInfo == null) {
            mAuthInfo = mCache.getJson("auth_info", AuthInfo.class);
        }
        return mAuthInfo;
    }

    public void setAuthInfo(AuthInfo info) {
        if (info != null) {
            mAuthInfo = info;
            mCache.setJson("auth_info", info);
        }
    }

    public void setUserInfo(UserInfo info) {
        if (info != null) {
            mUserInfo = info;
            mCache.setJson("user_info", info);
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
        UserInfo userInfo = getUserInfo();
        if (userInfo != null) {
            return userInfo.getToken();
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
