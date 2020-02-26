package com.vgaw.scaffold.http.service;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

/**
 * Created by caojin on 2017/5/25.
 */

public interface HttpService {
    @GET("{path}")
    Observable<ResponseBody> get(@Path(value = "path", encoded = true) String path, @HeaderMap Map<String, String> headerMap, @QueryMap Map<String, Object> queryMap);

    @GET("{path}")
    Observable<ResponseBody> get(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> post(@Path(value = "path", encoded = true) String path, @FieldMap Map<String, Object> fieldMap);

    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> post(@Path(value = "path", encoded = true) String path, @HeaderMap Map<String, String> headerMap, @FieldMap Map<String, Object> fieldMap);

    @POST("{path}")
    Observable<ResponseBody> postJson(@Path(value = "path", encoded = true) String path, @Body RequestBody requestBody);

    @POST("{path}")
    Observable<ResponseBody> postJson(@Path(value = "path", encoded = true) String path, @HeaderMap Map<String, String> headerMap, @Body RequestBody requestBody);

    @Multipart
    @POST("{path}")
    Observable<ResponseBody> upload(@Path(value = "path", encoded = true) String path, @Part MultipartBody.Part file, @HeaderMap Map<String, String> headerMap, @PartMap() Map<String, RequestBody> partMap);

    @Streaming
    @GET("{path}")
    Observable<ResponseBody> download(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> paramMap);
}
