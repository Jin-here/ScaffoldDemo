package com.vgaw.scaffold.http;

import com.vgaw.scaffold.http.callback.OnProgressUpdateListener;
import com.vgaw.scaffold.http.converter.FastJsonConvertFactory;
import com.vgaw.scaffold.http.interceptor.ProxyInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.internal.proxy.NullProxySelector;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by caojin on 2017/5/25.
 */

public class HttpHelper {
    public static <T> T buildService(final Class<T> service, OkHttpClient client, HttpConfig config) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getURL(config));
        if (client == null && config.debug()) {
            builder.client(getDefaultOkHttpClient(config));
        }
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync());
        builder.addConverterFactory(new FastJsonConvertFactory());
        return builder.build().create(service);
    }

    public static <T> T buildDownloadService(final Class<T> service, OkHttpClient client, HttpConfig config, OnProgressUpdateListener listener) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getURL(config));
        OkHttpClient.Builder clientBuilder = null;
        if (client == null) {
            clientBuilder = new OkHttpClient.Builder();
        } else {
            clientBuilder = client.newBuilder();
        }
        builder.client(getDownloadOkHttpClient(clientBuilder, listener));
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync());
        return builder.build().create(service);
    }

    private static OkHttpClient getDefaultOkHttpClient(HttpConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().proxySelector(new NullProxySelector());
        builder.addInterceptor(new ProxyInterceptor());
        if (config.debug()) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }

    /**
     * NOTE: 下载不添加日志拦截器
     * @param listener
     * @return
     */
    private static OkHttpClient getDownloadOkHttpClient(OkHttpClient.Builder builder, final OnProgressUpdateListener listener) {
        return builder
                .addInterceptor(chain -> {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), listener))
                            .build();
                }).build();
    }

    private static String getURL(HttpConfig config) {
        // scheme://host:port/path?query
        return config.debug() ? config.testUrl() : config.url();
    }
}
