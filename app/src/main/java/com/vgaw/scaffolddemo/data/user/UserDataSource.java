package com.vgaw.scaffolddemo.data.user;

import com.vgaw.scaffolddemo.data.callback.ListCallback;
import com.vgaw.scaffolddemo.data.callback.ObjectCallback;

public interface UserDataSource {
    void login(UserInfo userInfo, ObjectCallback<UserInfo> callback);

    void list(int offset, int pageSize, ListCallback<UserInfo> callback);
}
