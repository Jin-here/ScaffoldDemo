package com.vgaw.scaffold.page

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.vgaw.scaffold.R

open class ScaffoldFrag : Fragment() {
    protected var mActivity: FragmentActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity
    }

    protected inline fun <T> T.acValid(block: (T) -> Unit) {
        if (mActivity != null && !mActivity!!.isDestroyed) {
            block(this)
        }
    }

    protected inline fun <T> T.valid(block: (T) -> Unit) {
        if (!isDetached) {
            block(this)
        }
    }

    protected fun getSelf() = this

    /**
     * 当本fragment显示时，状态栏的颜色
     * @return
     */
    fun getStatusBarColor() = R.color.white
}
