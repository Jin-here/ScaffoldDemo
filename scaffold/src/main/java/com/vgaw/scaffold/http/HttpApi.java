package com.vgaw.scaffold.http;

import com.vgaw.scaffold.http.callback.OnProgressUpdateListener;
import com.vgaw.scaffold.http.interceptor.ProxyInterceptor;
import com.vgaw.scaffold.http.service.HttpService;
import com.vgaw.scaffold.json.JsonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.proxy.NullProxySelector;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

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

    public Call<ResponseBody> get(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = getService(client).get(path, headerMap, queryMap);
        call.enqueue(callback);
        return call;
    }

    public Call<ResponseBody> get(OkHttpClient client, String path, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = getService(client).get(path, queryMap);
        call.enqueue(callback);
        return call;
    }

    public Call<ResponseBody> post(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = getService(client).post(path, headerMap, fieldMap);
        call.enqueue(callback);
        return call;
    }

    public Call<ResponseBody> post(OkHttpClient client, String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = getService(client).post(path, fieldMap);
        call.enqueue(callback);
        return call;
    }

    public Call<ResponseBody> postJson(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        JsonUtil.toJson(fieldMap));
        Call<ResponseBody> call = getService(client).postJson(path, headerMap, requestBody);
        call.enqueue(callback);
        return call;
    }

    public Call<ResponseBody> postJson(OkHttpClient client, String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        JsonUtil.toJson(fieldMap));
        Call<ResponseBody> call = getService(client).postJson(path, requestBody);
        call.enqueue(callback);
        return call;
    }

    public Call<ResponseBody> upload(OkHttpClient client, String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, Callback<ResponseBody> callback) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        Set<String> keySet = fieldMap.keySet();
        for (String key : keySet) {
            String value = fieldMap.get(key);
            partMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), value));
        }
        Call<ResponseBody> call = getService(client).upload(path, partFile, headerMap, partMap);
        call.enqueue(callback);
        return call;
    }

    public Call<ResponseBody> upload(OkHttpClient client, String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        RequestBody requestFile = new ProgressRequestBody(file, MediaType.parse("multipart/form-data"), listener);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        Set<String> keySet = fieldMap.keySet();
        for (String key : keySet) {
            String value = fieldMap.get(key);
            partMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), value));
        }
        Call<ResponseBody> call = getService(client).upload(path, partFile, headerMap, partMap);
        call.enqueue(callback);
        return call;
    }

    /**
     * for big file downloading, small file downloading may use get method
     *
     * @return
     */
    public Call<ResponseBody> download(OkHttpClient client, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = getDownloadService(client, null, listener).download(path, paramMap);
        call.enqueue(callback);
        return call;
    }

    private Call<ResponseBody> download(OkHttpClient client, String baseUrl, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = getDownloadService(client, baseUrl, listener).download(path, paramMap);
        call.enqueue(callback);
        return call;
    }

    /*without client version*/

    public Call<ResponseBody> get(String path, Map<String, String> headerMap, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        return get(null, path, headerMap, queryMap, callback);
    }

    public Call<ResponseBody> get(String path, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        return get(null, path, queryMap, callback);
    }

    public Call<ResponseBody> post(String path, Map<String, String> headerMap, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        return post(null, path, headerMap, fieldMap, callback);
    }

    public Call<ResponseBody> post(String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        return post(null, path, fieldMap, callback);
    }

    public Call<ResponseBody> postJson(String path, Map<String, String> headerMap, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        return postJson(null, path, headerMap, fieldMap, callback);
    }

    public Call<ResponseBody> postJson(String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        return postJson(null, path, fieldMap, callback);
    }

    public Call<ResponseBody> upload(String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, Callback<ResponseBody> callback) {
        return upload(null, path, file, headerMap, fieldMap, callback);
    }

    public Call<ResponseBody> upload(String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        return upload(null, path, file, headerMap, fieldMap, listener, callback);
    }

    public Call<ResponseBody> download(String baseUrl, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        return download(null, baseUrl, path, paramMap, listener, callback);
    }

    private HttpService getService(OkHttpClient client) {
        // 此处没有保证线程安全，但是无所谓
        if (mService == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(getURL());
            if (client == null && mHttpConfig.debug()) {
                builder.client(getDefaultOkHttpClient());
            }
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
