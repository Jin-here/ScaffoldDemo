package com.vgaw.scaffold.page

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.Util

class ScaffoldDialog : DialogFragment() {
    companion object {
        private const val DEFAULT_SINGLE_BTN = false

        fun newInstance(title: String, @DrawableRes icon: Int = -1, des: String? = null) = newInstance(title, false, null, null, null, icon, des)

        fun newInstance(title: String, leftBtnDescription: String, rightBtnDescription: String, @DrawableRes icon: Int = -1, des: String? = null) = newInstance(title, false, null, leftBtnDescription, rightBtnDescription, icon, des)

        fun newInstance(title: String, singleBtnDescription: String, @DrawableRes icon: Int = -1, des: String? = null) = newInstance(title, true, singleBtnDescription, null, null, icon, des)

        private fun newInstance(title: String,
                                singleBtn: Boolean, singleBtnDescription: String?,
                                leftBtnDescription: String?, rightBtnDescription: String?,
                                @DrawableRes icon: Int = -1, des: String? = null): ScaffoldDialog {
            val args = Bundle()
            if (title != null) {
                args.putString("title", title)
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
            if (icon != -1) {
                args.putInt("icon", icon)
            }
            if (!TextUtils.isEmpty(des)) {
                args.putString("des", des)
            }

            val fragment = ScaffoldDialog()
            fragment.setArguments(args)
            return fragment
        }
    }

    private var mLeftBtnClickListener: View.OnClickListener? = null
    private var mRightBtnClickListener: View.OnClickListener? = null
    private var mSingleBtnClickListener: View.OnClickListener? = null

    private var mDes: String? = null
    private var mIcon = -1
    private var mTitle: String? = null
    private var mSingleBtn = false
    private var mSingleBtnDescription: String? = null
    private var mLeftBtnDescription: String? = null
    private var mRightBtnDescription: String? = null

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
            mTitle = it.getString("title")
            mSingleBtn = it.getBoolean("single_btn", DEFAULT_SINGLE_BTN)
            mSingleBtnDescription = it.getString("single_btn_description", getString(R.string.base_dialog_ok))
            mLeftBtnDescription = it.getString("left_btn_description", getString(R.string.base_dialog_cancel))
            mRightBtnDescription = it.getString("right_btn_description", getString(R.string.base_dialog_ok))
            mIcon = it.getInt("icon", -1)
            mDes = it.getString("des")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.base_dialog, container, false)
        val baseDialogIcon = view.findViewById<ImageView>(R.id.base_dialog_icon)
        val baseDialogTitle = view.findViewById<TextView>(R.id.base_dialog_title)
        val baseDialogDes = view.findViewById<TextView>(R.id.base_dialog_des)
        val baseDialogOk = view.findViewById<TextView>(R.id.base_dialog_ok)
        val baseDialogCancel = view.findViewById<TextView>(R.id.base_dialog_cancel)
        val baseDialogSingle = view.findViewById<TextView>(R.id.base_dialog_single)
        val baseDialogDivider = view.findViewById<View>(R.id.base_dialog_divider)

        if (!TextUtils.isEmpty(mDes)) {
            baseDialogDes.text = mDes
            baseDialogDes.visibility = View.VISIBLE
        }
        if (mIcon != -1) {
            baseDialogIcon.setImageResource(mIcon)
            baseDialogIcon.visibility = View.VISIBLE
        }
        if (!TextUtils.isEmpty(mTitle)) {
            baseDialogTitle.text = Util.nullToEmpty(mTitle)
            baseDialogTitle.visibility = View.VISIBLE
        } else {
            baseDialogTitle.visibility = View.GONE
        }

        if (mSingleBtn) {
            baseDialogCancel.visibility = View.GONE
            baseDialogOk.visibility = View.GONE
            baseDialogDivider.visibility = View.GONE
            baseDialogSingle.text = mSingleBtnDescription
            baseDialogSingle.setOnClickListener{v -> callSingleBtnClickListener(v)}
            baseDialogSingle.visibility = View.VISIBLE
        } else {
            baseDialogCancel.text = mLeftBtnDescription
            baseDialogOk.text = mRightBtnDescription
            baseDialogCancel.setOnClickListener{v -> callLeftBtnClickListener(v)}
            baseDialogOk.setOnClickListener{v -> callRigthBtnClickListener(v)}
            baseDialogCancel.visibility = View.VISIBLE
            baseDialogOk.visibility = View.VISIBLE
            baseDialogDivider.visibility = View.VISIBLE
            baseDialogSingle.visibility = View.GONE
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callDismiss(mBtnActionByCancel)
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
