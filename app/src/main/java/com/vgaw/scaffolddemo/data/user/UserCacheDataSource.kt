package com.vgaw.scaffolddemo.data.user

import com.vgaw.scaffold.util.cache.KVFileCache

class UserCacheDataSource {
    private val mFileCache: KVFileCache

    private constructor() {
        mFileCache = KVFileCache(NAME)
    }

    companion object {
        private const val NAME = "UserInfoCache"
        private val INSTANCE by lazy { UserCacheDataSource() }

        fun getInstance() = INSTANCE
    }

    private lateinit var mToken: String

    fun tokenValid(): Boolean {
        val token = mFileCache.get("token", null) ?: return false
        mToken = token
        return true
    }

    fun clear() {
        mFileCache.remove("user_info")
        mFileCache.remove("token")
    }

    fun setToken(token: String) {
        mToken = token
        mFileCache.set("token", token)
    }

    fun getToken(): String {
        return mToken
    }
}
