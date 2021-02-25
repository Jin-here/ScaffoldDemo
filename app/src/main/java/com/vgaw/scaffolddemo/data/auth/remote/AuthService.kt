package com.haoman.schedule.data.auth.remote;

import com.vgaw.scaffold.http.bean.HttpResult
import com.vgaw.scaffolddemo.data.auth.LoginResult
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by caojin on 2017/5/25.
 */

interface AuthService {
    @POST("{path}")
    @FormUrlEncoded
    @JvmSuppressWildcards
    fun login(@Path(value = "path", encoded = true) path: String,
              @HeaderMap headerMap: Map<String, String>,
              @FieldMap queryMap: Map<String, Any>): Call<HttpResult<LoginResult>>

    @GET("{path}")
    @JvmSuppressWildcards
    fun logout(@Path(value = "path", encoded = true) path: String,
               @QueryMap queryMap: Map<String, Any>): Call<HttpResult<Unit>>
}
