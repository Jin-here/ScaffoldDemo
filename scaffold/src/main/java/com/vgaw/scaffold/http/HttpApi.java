package com.vgaw.scaffold.http;

import com.vgaw.scaffold.http.callback.OnProgressUpdateListener;
import com.vgaw.scaffold.http.service.HttpService;
import com.vgaw.scaffold.json.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by caojin on 2017/5/25.
 */

public class HttpApi {
    private HttpConfig mHttpConfig;

    public void config(HttpConfig config) {
        mHttpConfig = config;
    }

    /**
     * small file download also use this
     *
     * @param path
     * @param queryMap
     * @return
     */
    public void get(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        getService(client).get(path, headerMap, queryMap).enqueue(callback);
    }

    public void get(OkHttpClient client, String path, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        getService(client).get(path, queryMap).enqueue(callback);
    }

    public void post(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        getService(client).post(path, headerMap, fieldMap).enqueue(callback);
    }

    public void post(OkHttpClient client, String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        getService(client).post(path, fieldMap).enqueue(callback);
    }

    public void postJson(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        JsonUtil.toJson(fieldMap));
        getService(client).postJson(path, headerMap, requestBody).enqueue(callback);
    }

    public void postJson(OkHttpClient client, String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        JsonUtil.toJson(fieldMap));
        getService(client).postJson(path, requestBody).enqueue(callback);
    }

    public void upload(OkHttpClient client, String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, Callback<ResponseBody> callback) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        Set<String> keySet = fieldMap.keySet();
        for (String key : keySet) {
            String value = fieldMap.get(key);
            partMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), value));
        }
        getService(client).upload(path, partFile, headerMap, partMap).enqueue(callback);
    }

    public void upload(OkHttpClient client, String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        RequestBody requestFile = new ProgressRequestBody(file, MediaType.parse("multipart/form-data"), listener);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        Set<String> keySet = fieldMap.keySet();
        for (String key : keySet) {
            String value = fieldMap.get(key);
            partMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), value));
        }
        getService(client).upload(path, partFile, headerMap, partMap).enqueue(callback);
    }

    /**
     * for big file downloading
     *
     * @return
     */
    public void download(OkHttpClient client, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        getDownloadService(client, null, listener).download(path, paramMap).enqueue(callback);
    }

    private void download(OkHttpClient client, String baseUrl, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        getDownloadService(client, baseUrl, listener).download(path, paramMap).enqueue(callback);
    }

    /*without client version*/

    public void get(String path, Map<String, String> headerMap, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        get(null, path, headerMap, queryMap, callback);
    }

    public void get(String path, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        get(null, path, queryMap, callback);
    }

    public void post(String path, Map<String, String> headerMap, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        post(null, path, headerMap, fieldMap, callback);
    }

    public void post(String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        post(null, path, fieldMap, callback);
    }

    public void postJson(String path, Map<String, String> headerMap, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        postJson(null, path, headerMap, fieldMap, callback);
    }

    public void postJson(String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        postJson(null, path, fieldMap, callback);
    }

    public void upload(String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, Callback<ResponseBody> callback) {
        upload(null, path, file, headerMap, fieldMap, callback);
    }

    public void upload(String path, File file, Map<String, String> headerMap, Map<String, String> fieldMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        upload(null, path, file, headerMap, fieldMap, listener, callback);
    }

    public void download(String baseUrl, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        download(null, baseUrl, path, paramMap, listener, callback);
    }

    /*without callback version*/

    public Response<ResponseBody> post(OkHttpClient client, String path, Map<String, String> headerMap, Map<String, Object> fieldMap) throws IOException {
        return getService(client).post(path, headerMap, fieldMap).execute();
    }

    public Response<ResponseBody> post(OkHttpClient client, String path, Map<String, Object> fieldMap) throws IOException {
        return getService(client).post(path, fieldMap).execute();
    }

    public Response<ResponseBody> get(OkHttpClient client, String path, Map<String, Object> queryMap) throws IOException {
        return getService(client).get(path, queryMap).execute();
    }

    private HttpService getService(OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getURL());
        if (client == null && mHttpConfig.debug()) {
            builder.client(getDefaultOkHttpClient());
        }
        return builder.build().create(HttpService.class);
    }

    private HttpService getDownloadService(OkHttpClient client, String baseUrl, OnProgressUpdateListener listener) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl == null ? getURL() : baseUrl);
        OkHttpClient.Builder clientBuilder = null;
        if (client == null) {
            clientBuilder = new OkHttpClient.Builder();
        } else {
            clientBuilder = client.newBuilder();
        }
        builder.client(getDownloadOkHttpClient(clientBuilder, listener));
        return builder.build().create(HttpService.class);
    }

    private OkHttpClient getDefaultOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().
                setLevel(HttpLoggingInterceptor.Level.BODY)).build();
    }

    /**
     * NOTE: 下载不添加日志拦截器
     * @param listener
     * @return
     */
    private OkHttpClient getDownloadOkHttpClient(OkHttpClient.Builder builder, final OnProgressUpdateListener listener) {
        return builder
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), listener))
                                .build();
                    }
                }).build();
    }

    private String getURL() {
        // scheme://host:port/path?query
        return mHttpConfig.debug() ? mHttpConfig.testUrl() : mHttpConfig.url();
    }
}
