package com.vgaw.scaffolddemo.data;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.vgaw.scaffold.util.context.ContextManager;
import com.vgaw.scaffolddemo.data.user.UserInfo;

public class PreferenceManager {
    private static SharedPreferences getDefault() {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(ContextManager.getInstance().getApplicationContext());
    }

    public static void clearUserInfo() {
        getDefault().edit()
                .remove("user_id")
                .remove("user_token")
                .remove("user_name")
                .remove("user_avatar")
                .commit();
    }

    public static UserInfo getUserInfo() {
        String userId = getUserId();
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setToken(getUserToken());
        userInfo.setName(getUserName());
        userInfo.setAvatar(getUserAvatar());
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        getDefault().edit()
                .putString("user_id", userInfo.getId())
                .putString("user_token", userInfo.getToken())
                .putString("user_name", userInfo.getName())
                .putString("user_avatar", userInfo.getAvatar())
                .commit();
    }

    private static String getUserToken() {
        return getDefault().getString("user_token", null);
    }

    private static String getUserId() {
        return getDefault().getString("user_id", null);
    }

    private static String getUserAvatar() {
        return getDefault().getString("user_avatar", null);
    }

    private static String getUserName() {
        return getDefault().getString("user_name", null);
    }
}
