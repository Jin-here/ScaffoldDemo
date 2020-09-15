package com.vgaw.scaffold.vm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.vgaw.scaffold.http.HttpContainer
import retrofit2.Call
import retrofit2.Callback

open class BaseVm : ViewModel(), LifecycleObserver {
    private var mHttpContainer: HttpContainer? = null

    protected fun <T> enqueueCall(call: Call<T>, callback: Callback<T>) {
        if (mHttpContainer == null) {
            mHttpContainer = HttpContainer()
        }
        mHttpContainer!!.enqueueCall(call, callback)
    }

    private fun cancelAllCall() {
        if (mHttpContainer != null) {
            mHttpContainer!!.cancelAllCall()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelAllCall()
    }
}