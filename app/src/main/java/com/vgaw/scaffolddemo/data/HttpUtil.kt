package com.vgaw.scaffolddemo.data

import android.text.TextUtils
import com.vgaw.scaffold.http.bean.HttpResult
import com.vgaw.scaffold.http.callback.Result
import com.vgaw.scaffold.json.JsonUtil
import com.vgaw.scaffolddemo.data.user.UserCacheDataSource
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException

class HttpUtil {
    companion object {
        suspend fun proResponseCode(response: Response<HttpResult<Unit>>): Result<Unit> {
            if (response.isSuccessful) {
                val responseBody: HttpResult<Unit>? = response.body()
                if (responseBody != null) {
                    if (HttpResult.success(responseBody)) {
                        return Result.buildSuccess(Unit)
                    } else {
                        return Result.buildError(responseBody.msg, responseBody.code)
                    }
                }
            } else {
                try {
                    return Result.buildError(response.errorBody()?.string())
                } catch (e: IOException) {}
            }
            return Result.buildError(null)
        }

        suspend fun <T> proResponse(response: Response<HttpResult<T>>): Result<T> {
            if (response.isSuccessful) {
                val responseBody: HttpResult<T>? = response.body()
                if (responseBody != null) {
                    if (HttpResult.success(responseBody)) {
                        val data = responseBody.body
                        data?.let { return Result.buildSuccess(it) }
                    } else {
                        return Result.buildError(responseBody.msg, responseBody.code)
                    }
                }
            } else {
                try {
                    return Result.buildError(response.errorBody()?.string())
                } catch (e: IOException) {}
            }
            return Result.buildError(null)
        }

        fun addDot(list: Collection<out Any>): String? {
            if (list.isEmpty()) {
                return null
            }
            val sb = StringBuilder()
            list.forEach { sb.append(it).append(",") }
            return sb.substring(0, sb.length - 1)
        }

        fun addNonNullAnyParam(map: HashMap<String, Any>, key: String, value: Any?) {
            if (value != null) {
                map[key] = value
            }
        }

        fun addNonNullParam(map: HashMap<String, RequestBody>, key: String, requestBody: RequestBody?) {
            if (requestBody != null) {
                map.put(key, requestBody)
            }
        }

        fun buildAuthorizationHeader(): HashMap<String, String> {
            val headerMap = HashMap<String, String>()
            headerMap.put("Authorization", buildToken())
            return headerMap
        }

        private fun buildToken() = String.format("Bearer %s", UserCacheDataSource.getInstance().getToken())

        fun buildJson(fieldMap: HashMap<String, Any>): RequestBody {
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    JsonUtil.toJson(fieldMap))

        }

        fun buildString(content: String?): RequestBody? {
            if (TextUtils.isEmpty(content)) {
                return null
            }
            return RequestBody.create(MediaType.parse("multipart/form-data"), content)
        }

        fun buildFile(filePath: String?): RequestBody? {
            if (TextUtils.isEmpty(filePath)) {
                return null
            }
            return RequestBody.create(MediaType.parse("multipart/form-data"), File(filePath))
        }
    }
}