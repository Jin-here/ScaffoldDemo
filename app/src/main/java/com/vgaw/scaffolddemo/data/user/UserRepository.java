package com.vgaw.scaffolddemo.data.user;

import com.vgaw.scaffolddemo.data.MockUtil;
import com.vgaw.scaffolddemo.data.callback.ListCallback;
import com.vgaw.scaffolddemo.data.callback.ObjectCallback;

import java.util.List;

/**
 * @author caojin
 * @date 2018/6/6
 */
public class UserRepository implements UserDataSource {
    private static UserRepository sInstance = null;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (sInstance == null) {
            synchronized (UserRepository.class) {
                if (sInstance == null) {
                    sInstance = new UserRepository();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void login(UserInfo userInfo, final ObjectCallback<UserInfo> callback) {
        UserInfo loginResult = MockUtil.buildLoginSucData();
        callback.onSuccess(loginResult);

        afterLogin(loginResult);
    }

    @Override
    public void list(int offset, int pageSize, ListCallback<UserInfo> callback) {
        List<UserInfo> userList = MockUtil.buildUserList();
        callback.onSuccess(userList);
    }

    private void afterLogin(UserInfo accountInfo) {
        UserInfoCache.getInstance().setUserInfo(accountInfo);
    }
}
