package com.vgaw.scaffolddemo.data.auth;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface AuthDataSource {
    Observable<ResponseBody> login(String account, String password);
}
