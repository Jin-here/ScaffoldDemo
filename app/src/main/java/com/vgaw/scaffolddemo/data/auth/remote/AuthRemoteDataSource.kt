package com.haoman.schedule.data.auth.remote

import com.vgaw.scaffold.BuildConfig
import com.vgaw.scaffold.http.HttpConfig
import com.vgaw.scaffold.http.HttpHelper
import com.vgaw.scaffold.http.bean.HttpResult
import com.vgaw.scaffolddemo.data.HttpConstant
import com.vgaw.scaffolddemo.data.auth.LoginResult
import retrofit2.Response

class AuthRemoteDataSource {
    private val mService: AuthService

    private constructor() {
        val httpConfig = HttpConfig()
        httpConfig.url(HttpConstant.URL)
        httpConfig.testUrl(HttpConstant.URL)
        httpConfig.debug(BuildConfig.DEBUG)
        mService = HttpHelper.buildService(AuthService::class.java, null, httpConfig)
    }

    companion object {
        private val INSTANCE by lazy { AuthRemoteDataSource() }

        fun getInstance() = INSTANCE
    }

    fun login(accountNo: String, authorization: String, password: String): Response<HttpResult<LoginResult>> {
        val map = HashMap<String, Any>()
        map.put("accountNo", accountNo)
        map.put("password", password)

        val headerMap = HashMap<String, String>()
        headerMap.put("Authorization", authorization)
        return mService.login("/account/token", headerMap, map).execute()
    }
}