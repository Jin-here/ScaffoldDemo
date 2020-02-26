package com.vgaw.scaffolddemo.http.api;

import com.vgaw.scaffold.http.HttpApi;
import com.vgaw.scaffold.http.HttpConfig;
import com.vgaw.scaffold.http.callback.OnProgressUpdateListener;
import com.vgaw.scaffolddemo.BuildConfig;
import com.vgaw.scaffolddemo.data.user.UserInfoCache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * @author caojin
 * @date 2018/6/2
 */
public class AppApi {
    private static final String URL = "https://xiaoshijie.weezhi.cn/";
    private static final String URL_TEST = "https://xiaoshijie.weezhi.cn/";
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

    public static Observable<ResponseBody> noAuthPostJson(String path) {
        Map<String, Object> map = new HashMap<>();
        return noAuthPostJson(path, map);
    }

    public static Observable<ResponseBody> noAuthPostJson(String path, Map<String, Object> fieldMap) {
        Map<String, String> headerMap = new HashMap<>();
        return sHttpApi.postJson(getFullPath(path), headerMap, fieldMap);
    }

    public static Observable<ResponseBody> postJson(String path) {
        Map<String, Object> map = new HashMap<>();
        return postJson(path, map);
    }

    public static Observable<ResponseBody> postJson(String path, Map<String, Object> fieldMap) {
        Map<String, String> headerMap = new HashMap<>();
        String token = UserInfoCache.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put("Authorization", buildToken(token));
        return sHttpApi.postJson(getFullPath(path), headerMap, fieldMap);
    }

    public static Observable<ResponseBody> noAuthGet(String path) {
        Map<String, Object> map = new HashMap<>();
        return noAuthGet(path, map);
    }

    public static Observable<ResponseBody> noAuthGet(String path, Map<String, Object> queryMap) {
        Map<String, String> headerMap = new HashMap<>();
        return sHttpApi.get(getFullPath(path), headerMap, queryMap);
    }

    public static Observable<ResponseBody> get(String path) {
        Map<String, Object> map = new HashMap<>();
        return get(path, map);
    }

    public static Observable<ResponseBody> get(String path, Map<String, Object> queryMap) {
        Map<String, String> headerMap = new HashMap<>();
        String token = UserInfoCache.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put("Authorization", buildToken(token));
        return sHttpApi.get(getFullPath(path), headerMap, queryMap);
    }

    public static Observable<ResponseBody> noAuthUpload(String path, File file, OnProgressUpdateListener listener) {
        Map<String, String> map = new HashMap<>();
        return noAuthUpload(path, file, map, listener);
    }

    public static Observable<ResponseBody> noAuthUpload(String path, File file, Map<String, String> fieldMap, OnProgressUpdateListener listener) {
        Map<String, String> headerMap = new HashMap<>();
        return sHttpApi.upload(getFullPath(path), file, headerMap, fieldMap, listener);
    }

    public static Observable<ResponseBody> upload(String path, File file, OnProgressUpdateListener listener) {
        Map<String, String> map = new HashMap<>();
        return upload(path, file, map, listener);
    }

    public static Observable<ResponseBody> upload(String path, File file, Map<String, String> fieldMap, OnProgressUpdateListener listener) {
        Map<String, String> headerMap = new HashMap<>();
        String token = UserInfoCache.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put("Authorization", buildToken(token));
        return sHttpApi.upload(getFullPath(path), file, headerMap, fieldMap, listener);
    }

    public static Observable<ResponseBody> download(String baseUrl, String path, OnProgressUpdateListener listener) {
        return sHttpApi.download(baseUrl, path, new HashMap<String, Object>(), listener);
    }

    private static String getFullPath(String raw) {
        return raw;
    }

    private static String buildToken(String raw) {
        return raw;
    }
}