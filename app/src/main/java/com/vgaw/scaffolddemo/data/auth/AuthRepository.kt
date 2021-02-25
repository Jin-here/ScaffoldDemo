package com.vgaw.scaffolddemo.data.auth

import com.haoman.schedule.data.auth.remote.AuthRemoteDataSource
import com.vgaw.scaffold.http.callback.Result
import com.vgaw.scaffolddemo.data.HttpUtil
import com.vgaw.scaffolddemo.data.user.UserCacheDataSource

class AuthRepository {
    private constructor()

    companion object {
        private val INSTANCE by lazy { AuthRepository() }

        fun getInstance() = INSTANCE
    }

    suspend fun login(accountNo: String, authorization: String, password: String): Result<Unit> {
        try {
            val result = HttpUtil.proResponse(AuthRemoteDataSource.getInstance().login(accountNo, authorization, password)!!)
            if (result.code == Result.ResultCode.SUCCESS) {
                UserCacheDataSource.getInstance().setToken(result.data.accessToken)
                return Result.buildSuccess(Unit)
            } else {
                return Result.buildError(result.error)
            }
        } catch (e: Exception) {
            return Result.buildError(e.localizedMessage)
        }
    }
}