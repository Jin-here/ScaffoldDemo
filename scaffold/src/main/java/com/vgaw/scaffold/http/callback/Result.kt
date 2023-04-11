package com.vgaw.scaffold.http.callback

import com.vgaw.scaffold.view.ScaffoldToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Result<T>(val code: ResultCode, private val _data: T? = null, val error: String? = null) {
    val data: T
        get() = _data!!

    companion object {
        val sErrorMap = mapOf(
                // TODO: 2/25/21 put your error code map here
                Pair(70010, "该账号已被登录"),
                Pair(70030, "该用户不存在"),
        )

        fun <T> buildSuccess(result: T) = Result<T>(ResultCode.SUCCESS, result)

        suspend fun <T> buildError(msg: String?, code: Int? = null): Result<T> {
            var error: String? = null
            if (code != null) {
                error = sErrorMap[code]
            }
            if (error == null) {
                error = msg
            }
            if (error != null) {
                withContext(Dispatchers.Main) {
                    ScaffoldToast.show(error)
                }
            }
            return Result<T>(ResultCode.ERROR, error = error)
        }
    }

    enum class ResultCode {
        SUCCESS,
        ERROR;
    }
}