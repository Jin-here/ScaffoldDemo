package com.vgaw.scaffolddemo.http;

import com.vgaw.scaffold.http.HttpApi;
import com.vgaw.scaffold.http.HttpConfig;
import com.vgaw.scaffold.http.callback.OnProgressUpdateListener;
import com.vgaw.scaffolddemo.BuildConfig;
import com.vgaw.scaffolddemo.data.user.UserInfoCache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
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

    public static void noAuthPostJson(String path, JsonCallback callback) {
        Map<String, Object> map = new HashMap<>();
        noAuthPostJson(path, map, callback);
    }

    public static void noAuthPostJson(String path, Map<String, Object> fieldMap, JsonCallback callback) {
        Map<String, String> headerMap = new HashMap<>();
        sHttpApi.postJson(getFullPath(path), headerMap, fieldMap, callback);
    }

    public static void postJson(String path, JsonCallback callback) {
        Map<String, Object> map = new HashMap<>();
        postJson(path, map, callback);
    }

    public static void postJson(String path, Map<String, Object> fieldMap, JsonCallback callback) {
        Map<String, String> headerMap = new HashMap<>();
        String token = UserInfoCache.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put("Authorization", buildToken(token));
        sHttpApi.postJson(getFullPath(path), headerMap, fieldMap, callback);
    }

    public static void noAuthGet(String path, Callback<ResponseBody> callback) {
        Map<String, Object> map = new HashMap<>();
        noAuthGet(path, map, callback);
    }

    public static void noAuthGet(String path, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        sHttpApi.get(getFullPath(path), headerMap, queryMap, callback);
    }

    public static void get(String path, Callback<ResponseBody> callback) {
        Map<String, Object> map = new HashMap<>();
        get(path, map, callback);
    }

    public static void get(String path, Map<String, Object> queryMap, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        String token = UserInfoCache.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put("Authorization", buildToken(token));
        sHttpApi.get(getFullPath(path), headerMap, queryMap, callback);
    }

    public static void noAuthUpload(String path, File file, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>();
        noAuthUpload(path, file, map, listener, callback);
    }

    public static void noAuthUpload(String path, File file, Map<String, String> fieldMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        sHttpApi.upload(getFullPath(path), file, headerMap, fieldMap, listener, callback);
    }

    public static void upload(String path, File file, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>();
        upload(path, file, map, listener, callback);
    }

    public static void upload(String path, File file, Map<String, String> fieldMap, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        Map<String, String> headerMap = new HashMap<>();
        String token = UserInfoCache.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put("Authorization", buildToken(token));
        sHttpApi.upload(getFullPath(path), file, headerMap, fieldMap, listener, callback);
    }

    public static void download(String baseUrl, String path, OnProgressUpdateListener listener, Callback<ResponseBody> callback) {
        sHttpApi.download(baseUrl, path, new HashMap<String, Object>(), listener, callback);
    }

    private static String getFullPath(String raw) {
        return String.format("%s%s", URL_SUFFIX, raw);
    }

    private static String buildToken(String raw) {
        return raw;
    }
}