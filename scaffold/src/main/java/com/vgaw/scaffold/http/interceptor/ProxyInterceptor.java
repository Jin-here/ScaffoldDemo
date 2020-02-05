package com.vgaw.scaffold.http.interceptor;

import com.vgaw.scaffold.util.net.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ProxyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (NetworkUtil.isWifiProxy()) {
            chain.call().cancel();
        }
        return chain.proceed(chain.request());
    }
}
