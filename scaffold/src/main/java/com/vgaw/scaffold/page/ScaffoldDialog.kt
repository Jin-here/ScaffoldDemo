package com.vgaw.scaffold.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.Util
import kotlinx.android.synthetic.main.base_dialog.view.*

/**
 * 1. 如果有自定义View，推荐使用继承方式
 * 2. 推荐通过newInstance()方式使用
 */
open class ScaffoldDialog : DialogFragment() {
    companion object {
        private const val DEFAULT_SHOW_DESCRIPTION = true
        private const val DEFAULT_SINGLE_BTN = false

        fun newInstance(description: String) = Companion.newInstance(true, description, false, null, null, null)

        fun newInstance(description: String, leftBtnDescription: String, rightBtnDescription: String) = newInstance(true, description, false, null, leftBtnDescription, rightBtnDescription)

        fun newInstance(description: String, singleBtnDescription: String) = newInstance(true, description, true, singleBtnDescription, null, null)

        private fun newInstance(showDescription: Boolean, description: String,
                                singleBtn: Boolean, singleBtnDescription: String?,
                                leftBtnDescription: String?, rightBtnDescription: String?): ScaffoldDialog {
            val args = Bundle()
            args.putBoolean("show_description", showDescription)
            if (description != null) {
                args.putString("description", description)
            }
            args.putBoolean("single_btn", singleBtn)
            if (singleBtnDescription != null) {
                args.putString("single_btn_description", singleBtnDescription)
            }
            if (leftBtnDescription != null) {
                args.putString("left_btn_description", leftBtnDescription)
            }
            if (rightBtnDescription != null) {
                args.putString("right_btn_description", rightBtnDescription)
            }

            val fragment = ScaffoldDialog()
            fragment.setArguments(args)
            return fragment
        }
    }

    private var mLeftBtnClickListener: View.OnClickListener? = null
    private var mRightBtnClickListener: View.OnClickListener? = null
    private var mSingleBtnClickListener: View.OnClickListener? = null

    private var mShowDescription = false
    private var mDescription: String? = null
    private var mSingleBtn = false
    private var mSingleBtnDescription: String? = null
    private var mLeftBtnDescription: String? = null
    private var mRightBtnDescription: String? = null
    private var mMainContent: View? = null

    private var mListener: OnDismissListener? = null
    private var mBtnActionByCancel = false

    fun setBtnActionByCancel(byCancel: Boolean) {
        mBtnActionByCancel = byCancel
    }

    fun setOnDismissListener(listener: OnDismissListener?) {
        mListener = listener
    }

    fun setOnLeftBtnClickListener(listener: View.OnClickListener?) {
        mLeftBtnClickListener = listener
    }

    fun setOnRightBtnClickListener(listener: View.OnClickListener?) {
        mRightBtnClickListener = listener
    }

    fun setOnSingleBtnClickListener(listener: View.OnClickListener?) {
        mSingleBtnClickListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        args?.let {
            mShowDescription = it.getBoolean("show_description", DEFAULT_SHOW_DESCRIPTION)
            mDescription = it.getString("description")
            mSingleBtn = it.getBoolean("single_btn", DEFAULT_SINGLE_BTN)
            mSingleBtnDescription = it.getString("single_btn_description", getString(R.string.base_dialog_ok))
            mLeftBtnDescription = it.getString("left_btn_description", getString(R.string.base_dialog_cancel))
            mRightBtnDescription = it.getString("right_btn_description", getString(R.string.base_dialog_ok))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate (R.layout.base_dialog, container, false)
        if (mShowDescription) {
            view.baseDialogDescription.text = Util.nullToEmpty(mDescription)
            view.baseDialogDescription.visibility = View.VISIBLE
        } else {
            view.baseDialogDescription.visibility = View.GONE
        }

        if (mMainContent != null) {
            view.baseDialogContent.addView(mMainContent, 1)
        }

        if (mSingleBtn) {
            view.baseDialogCancel.visibility = View.GONE
            view.baseDialogOk.visibility = View.GONE
            view.baseDialogSingle.text = mSingleBtnDescription
            view.baseDialogSingle.setOnClickListener{v -> callSingleBtnClickListener(v)}
            view.baseDialogSingle.visibility = View.VISIBLE
        } else {
            view.baseDialogCancel.text = mLeftBtnDescription
            view.baseDialogOk.text = mRightBtnDescription
            view.baseDialogCancel.setOnClickListener{v -> callLeftBtnClickListener(v)}
            view.baseDialogOk.setOnClickListener{v -> callRigthBtnClickListener(v)}
            view.baseDialogCancel.visibility = View.VISIBLE
            view.baseDialogOk.visibility = View.VISIBLE
            view.baseDialogSingle.visibility = View.GONE
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callDismiss(mBtnActionByCancel)
    }

    /**
     * 该处获得的view显示在顶部描述和底部按钮之间
     * @return
     */
    protected fun setMainContent(mainContent: View) {
        mMainContent = mainContent
    }

    /**
     * 是否显示位于顶部的描述
     * @return
     */
    protected fun setShowDescription(showDescription: Boolean) {
        mShowDescription = showDescription
    }

    /**
     * 设置位于顶部的描述
     * @return
     */
    protected fun setDescription(description: String) {
        mDescription = description
    }

    /**
     * 底部是否仅显示一个按钮，即"取消"和"确定"按钮合并成一个大按钮
     * @return
     */
    protected fun setSingleBtn(singleBtn: Boolean) {
        mSingleBtn = singleBtn
    }

    protected fun setSingleBtnDescription(singleBtnDescription: String) {
        mSingleBtnDescription = singleBtnDescription
    }

    protected fun setLeftBtnDescription(leftBtnDescription: String) {
        mLeftBtnDescription = leftBtnDescription
    }

    protected fun setRightBtnDescription(rightBtnDescription: String) {
        mRightBtnDescription = rightBtnDescription
    }

    private fun callLeftBtnClickListener(v: View) {
        mLeftBtnClickListener?.onClick(v)
    }

    private fun callRigthBtnClickListener(v: View) {
        mRightBtnClickListener?.onClick(v)
    }

    private fun callSingleBtnClickListener(v: View) {
        mSingleBtnClickListener?.onClick(v)
    }

    private fun callDismiss(byCancel: Boolean) {
        mListener?.onDismiss(byCancel)
    }

    interface OnDismissListener {
        /**
         * 该点击是否是Cancel，可通过在点击回调中调用{@link #setBtnActionByCancel(boolean)}进行设置
         * @param byCancel
         */
        fun onDismiss(byCancel: Boolean)
    }
}
