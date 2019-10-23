package com.vgaw.scaffolddemo.data.user;

import com.vgaw.scaffolddemo.data.PreferenceManager;

public class UserInfoCache {
    private static UserInfoCache sInstance;

    private UserInfo mUserInfo;

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

        }
        return mUserInfo;
    }

    public void setUserInfo(UserInfo info) {
        if (info != null) {
            mUserInfo = info;
            PreferenceManager.setUserInfo(info);
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
