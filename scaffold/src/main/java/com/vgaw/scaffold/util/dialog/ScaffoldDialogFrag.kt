package com.vgaw.scaffold.util.dialog

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

open class ScaffoldDialogFrag : DialogFragment() {
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
}