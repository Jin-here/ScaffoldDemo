package com.vgaw.scaffolddemo.http.api;

import com.vgaw.scaffold.http.HttpApi;
import com.vgaw.scaffold.http.HttpConfig;
import com.vgaw.scaffold.http.callback.OnProgressUpdateListener;
import com.vgaw.scaffolddemo.BuildConfig;
import com.vgaw.scaffolddemo.data.user.UserInfoCache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author caojin
 * @date 2018/6/2
 */
public class AppApi {
    private static final String URL = "https://test.com/";
    private static final String URL_TEST = "https://test.com/";
    private static final String URL_SUFFIX = "test";
    private static HttpApi sHttpApi;
    private static HttpConfig sHttpConfig;

    public static void init() {
        sHttpApi = new HttpApi();
        sHttpConfig = new HttpConfig()
                .debug(BuildConfig.DEBUG)
                .url(URL)
                .testUrl(URL_TEST);
        sHttpApi.config(sHttpConfig);
    }

    public static Call<ResponseBody> noAuthPostJson(String path, Callback<ResponseBody> callback) {
        Map<String, Object> map = new HashMap<>();
        return noAuthPostJson(path, map, callback);
    }

    public static Call<ResponseBody> noAuthPostJson(String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        return sHttpApi.postJson(getFullPath(path), headerMap, fieldMap, callback);
    }

    public static Call<ResponseBody> postJson(String path, Callback<ResponseBody> callback) {
        Map<String, Object> map = new HashMap<>();
        return postJson(path, map, callback);
    }

    public static Call<ResponseBody> postJson(String path, Map<String, Object> fieldMap, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        String token = UserInfoCache.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put("Authorization", buildToken(token));
        return sHttpApi.postJson(getFullPath(path), headerMap, fieldMap, callback);
    }

    public static Call<ResponseBody> noAuthGet(String path, Callback<ResponseBody> callback) {
        Map<String, Object> map = new HashMap<>();
        return noAuthGet(path, map, callback);
    }

    public static Call<ResponseBody> noAuthGet(String path, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        return sHttpApi.get(getFullPath(path), headerMap, queryMap, callback);
    }

    public static Call<ResponseBody> get(String path, Callback<ResponseBody> callback) {
        Map<String, Object> map = new HashMap<>();
        return get(path, map, callback);
    }

    public static Call<ResponseBody> get(String path, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        String token = UserInfoCache.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put("Authorization", buildToken(token));
        return sHttpApi.get(getFullPath(path), headerMap, queryMap, callback);
    }

    public static Call<ResponseBody> noAuthUpload(String path, File file, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>();
        return noAuthUpload(path, file, map, listener, callback);
    }

    public static Call<ResponseBody> noAuthUpload(String path, File file, Map<String, String> fieldMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        return sHttpApi.upload(getFullPath(path), file, headerMap, fieldMap, listener, callback);
    }

    public static Call<ResponseBody> upload(String path, File file, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>();
        return upload(path, file, map, listener, callback);
    }

    public static Call<ResponseBody> upload(String path, File file, Map<String, String> fieldMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        String token = UserInfoCache.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put("Authorization", buildToken(token));
        return sHttpApi.upload(getFullPath(path), file, headerMap, fieldMap, listener, callback);
    }

    public static Call<ResponseBody> download(String baseUrl, String path, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        return sHttpApi.download(baseUrl, path, new HashMap<String, Object>(), listener, callback);
    }

    private static String getFullPath(String raw) {
        return String.format("%s%s", URL_SUFFIX, raw);
    }

    private static String buildToken(String raw) {
        return raw;
    }
}