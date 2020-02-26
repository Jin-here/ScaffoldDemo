package com.vgaw.scaffolddemo.data.user;

/**
 * @author caojin
 * @date 2018/6/6
 */
public class UserRepository implements UserDataSource {
    private static volatile UserRepository sInstance = null;

    private UserRepository() {}

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
}
