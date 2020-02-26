package com.vgaw.scaffold.http;

import com.vgaw.scaffold.http.callback.OnProgressUpdateListener;
import com.vgaw.scaffold.http.interceptor.ProxyInterceptor;
import com.vgaw.scaffold.http.service.HttpService;
import com.vgaw.scaffold.json.JsonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.proxy.NullProxySelector;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by caojin on 2017/5/25.
 * 不同地址的请求需使用不同的实例
 */

public class HttpApi {
    private HttpConfig mHttpConfig;

    private HttpService mService;
    private HttpService mDownloadService;

    public void config(HttpConfig config) {
        mHttpConfig = config;
    }

    public Observable<ResponseBody> get(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> queryMap) {
        return getService(client).get(path, headerMap, queryMap);
    }

    public Observable<ResponseBody> get(OkHttpClient client, String path, Map<String, Object> queryMap) {
        return getService(client).get(path, queryMap);
    }

    public Observable<ResponseBody> post(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> fieldMap) {
        return getService(client).post(path, headerMap, fieldMap);
    }

    public Observable<ResponseBody> post(OkHttpClient client, String path, Map<String, Object> fieldMap) {
        return getService(client).post(path, fieldMap);
    }

    public Observable<ResponseBody> postJson(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> fieldMap) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        JsonUtil.toJson(fieldMap));
        return getService(client).postJson(path, headerMap, requestBody);
    }

    public Observable<ResponseBody> postJson(OkHttpClient client, String path, Map<String, Object> fieldMap) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        JsonUtil.toJson(fieldMap));
        return getService(client).postJson(path, requestBody);
    }

    public Observable<ResponseBody> upload(OkHttpClient client, String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        Set<String> keySet = fieldMap.keySet();
        for (String key : keySet) {
            String value = fieldMap.get(key);
            partMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), value));
        }
        return getService(client).upload(path, partFile, headerMap, partMap);
    }

    public Observable<ResponseBody> upload(OkHttpClient client, String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, OnProgressUpdateListener listener) {
        RequestBody requestFile = new ProgressRequestBody(file, MediaType.parse("multipart/form-data"), listener);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        Set<String> keySet = fieldMap.keySet();
        for (String key : keySet) {
            String value = fieldMap.get(key);
            partMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), value));
        }
        return getService(client).upload(path, partFile, headerMap, partMap);
    }

    /**
     * for big file downloading, small file downloading may use get method
     *
     * @return
     */
    public Observable<ResponseBody> download(OkHttpClient client, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener) {
        return getDownloadService(client, null, listener).download(path, paramMap);
    }

    private Observable<ResponseBody> download(OkHttpClient client, String baseUrl, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener) {
        return getDownloadService(client, baseUrl, listener).download(path, paramMap);
    }

    /*without client version*/

    public Observable<ResponseBody> get(String path, Map<String, String> headerMap, Map<String, Object> queryMap) {
        return get(null, path, headerMap, queryMap);
    }

    public Observable<ResponseBody> get(String path, Map<String, Object> queryMap) {
        return get(null, path, queryMap);
    }

    public Observable<ResponseBody> post(String path, Map<String, String> headerMap, Map<String, Object> fieldMap) {
        return post(null, path, headerMap, fieldMap);
    }

    public Observable<ResponseBody> post(String path, Map<String, Object> fieldMap) {
        return post(null, path, fieldMap);
    }

    public Observable<ResponseBody> postJson(String path, Map<String, String> headerMap, Map<String, Object> fieldMap) {
        return postJson(null, path, headerMap, fieldMap);
    }

    public Observable<ResponseBody> postJson(String path, Map<String, Object> fieldMap) {
        return postJson(null, path, fieldMap);
    }

    public Observable<ResponseBody> upload(String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap) {
        return upload(null, path, file, headerMap, fieldMap);
    }

    public Observable<ResponseBody> upload(String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, OnProgressUpdateListener listener) {
        return upload(null, path, file, headerMap, fieldMap, listener);
    }

    public Observable<ResponseBody> download(String baseUrl, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener) {
        return download(null, baseUrl, path, paramMap, listener);
    }

    private HttpService getService(OkHttpClient client) {
        // 此处没有保证线程安全，但是无所谓
        if (mService == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(getURL());
            if (client == null && mHttpConfig.debug()) {
                builder.client(getDefaultOkHttpClient());
            }
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync());
            mService = builder.build().create(HttpService.class);
        }
        return mService;
    }

    private HttpService getDownloadService(OkHttpClient client, String baseUrl, OnProgressUpdateListener listener) {
        // 此处没有保证线程安全，但是无所谓
        if (mDownloadService == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl == null ? getURL() : baseUrl);
            OkHttpClient.Builder clientBuilder = null;
            if (client == null) {
                clientBuilder = new OkHttpClient.Builder();
            } else {
                clientBuilder = client.newBuilder();
            }
            builder.client(getDownloadOkHttpClient(clientBuilder, listener));
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync());
            mDownloadService = builder.build().create(HttpService.class);
        }
        return mDownloadService;
    }

    private OkHttpClient getDefaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().proxySelector(new NullProxySelector());
        builder.addInterceptor(new ProxyInterceptor());
        if (mHttpConfig.debug()) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }

    /**
     * NOTE: 下载不添加日志拦截器
     * @param listener
     * @return
     */
    private OkHttpClient getDownloadOkHttpClient(OkHttpClient.Builder builder, final OnProgressUpdateListener listener) {
        return builder
                .addInterceptor(chain -> {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), listener))
                            .build();
                }).build();
    }

    private String getURL() {
        // scheme://host:port/path?query
        return mHttpConfig.debug() ? mHttpConfig.testUrl() : mHttpConfig.url();
    }
}
