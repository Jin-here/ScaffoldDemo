package com.vgaw.scaffold.page

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.vgaw.scaffold.http.HttpContainer
import com.vgaw.scaffold.util.phone.ImmerseUtil
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import retrofit2.Call
import retrofit2.Callback

open class ScaffoldAc : AppCompatActivity() {
    private var mImmerse = false
    private var mKeepScreenOn = false
    private var mDarkMode = false
    private val mHandler by lazy(LazyThreadSafetyMode.NONE) { Handler() }
    private var mHttpContainer: HttpContainer? = null

    protected fun enableImmerse() {
        mImmerse = true
    }

    protected fun keepScreenOn() {
        mKeepScreenOn = true
    }

    protected fun makeDarkMode() {
        mDarkMode = true
    }

    protected fun getSelf(): AppCompatActivity = this

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

    override fun onCreate(savedInstanceState: Bundle?) {
        if (mKeepScreenOn) {
            // 保持屏幕常亮
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        if (mImmerse) {
            // 开启沉浸式
            window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility == 0) {
                    mHandler.removeCallbacks(mEnterImmerseModeRunnable)
                    mHandler.postDelayed(mEnterImmerseModeRunnable, 2000)
                }
            }
            ImmerseUtil.startImmerse(this)
        }
        super.onCreate(savedInstanceState)
        if (mDarkMode) {
            StatusBarUtil.setDarkMode(this)
        } else {
            StatusBarUtil.setLightMode(this)
        }
    }

    override fun onDestroy() {
        // 关闭屏幕常亮
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing) {
            cancelAllCall()
        }
    }

    private val mEnterImmerseModeRunnable = Runnable { ImmerseUtil.startImmerse(getSelf()) }
}
