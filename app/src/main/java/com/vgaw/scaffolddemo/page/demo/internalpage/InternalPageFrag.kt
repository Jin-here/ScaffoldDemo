package com.vgaw.scaffolddemo.page.demo.internalpage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vgaw.scaffold.page.MockFrag
import com.vgaw.scaffold.page.ScaffoldDialog
import com.vgaw.scaffold.page.common.ImgPreviewAc
import com.vgaw.scaffold.page.common.PermissionAc
import com.vgaw.scaffold.page.common.chooseimg.ChooseImgAc
import com.vgaw.scaffold.page.qrcode.ScanAc
import com.vgaw.scaffold.util.context.ContextManager
import com.vgaw.scaffold.util.dialog.BottomMenuDialog
import com.vgaw.scaffold.util.dialog.DialogUtil
import com.vgaw.scaffold.util.dialog.ProgressDialogUtil
import com.vgaw.scaffolddemo.R
import com.vgaw.scaffolddemo.page.feedback.FeedbackAc
import timber.log.Timber

class InternalPageFrag : MockFrag() {
    companion object {
        private const val REQUEST_CODE_PERMISSION = 0
        private const val REQUEST_CODE_CHOOSE_IMG = 1
        private const val REQUEST_CODE_SCAN = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.internal_page_frag, container, false)
        view.findViewById<View>(R.id.internal_page_scan).setOnClickListener {ScanAc.startActivityForResult(getSelf(), REQUEST_CODE_SCAN)}
        view.findViewById<View>(R.id.internal_page_choose_img).setOnClickListener {ChooseImgAc.startActivityForResult(getSelf(), REQUEST_CODE_CHOOSE_IMG, null)}
        view.findViewById<View>(R.id.internal_page_check_version).setOnClickListener {}
        view.findViewById<View>(R.id.internal_page_feedback).setOnClickListener {FeedbackAc.startActivity(getSelf())}
        view.findViewById<View>(R.id.internal_page_permission).setOnClickListener {requestPermission()}
        view.findViewById<View>(R.id.internal_page_img_preview).setOnClickListener {ImgPreviewAc.startAc(getSelf(), buildImgList())}
        view.findViewById<View>(R.id.internal_page_base_dialog).setOnClickListener {
            val dialog = ScaffoldDialog.newInstance("标题")
            dialog.setOnLeftBtnClickListener { DialogUtil.dismissDialog(dialog) }
            dialog.setOnRightBtnClickListener { DialogUtil.dismissDialog(dialog) }
            DialogUtil.showDialog(dialog, mActivity!!.supportFragmentManager, false)
        }
        view.findViewById<View>(R.id.internal_page_base_dialog1).setOnClickListener {
            val dialog = ScaffoldDialog.newInstance("标题", des = "12345678910123456789101234567891012345678910")
            dialog.setOnLeftBtnClickListener { DialogUtil.dismissDialog(dialog) }
            dialog.setOnRightBtnClickListener { DialogUtil.dismissDialog(dialog) }
            DialogUtil.showDialog(dialog, mActivity!!.supportFragmentManager, false)
        }
        view.findViewById<View>(R.id.internal_page_base_dialog2).setOnClickListener {
            val dialog = ScaffoldDialog.newInstance("标题", "了解", android.R.drawable.ic_dialog_alert, "12345678910123456789101234567891012345678910")
            dialog.setOnSingleBtnClickListener { DialogUtil.dismissDialog(dialog) }
            DialogUtil.showDialog(dialog, mActivity!!.supportFragmentManager, false)
        }
        view.findViewById<View>(R.id.internal_page_bottom_dialog).setOnClickListener {
            val dialog = BottomMenuDialog.newInstance("标题", arrayListOf("1s", "2s", "3s", "4s"))
            dialog.showDialog(mActivity!!.supportFragmentManager)
        }
        view.findViewById<View>(R.id.internal_page_bottom_dialog1).setOnClickListener {
            val dialog = BottomMenuDialog.newInstance(arrayListOf("1s", "2s", "3s", "4s"))
            dialog.showDialog(mActivity!!.supportFragmentManager)
        }
        view.findViewById<View>(R.id.internal_page_progress_dialog).setOnClickListener {
            ProgressDialogUtil.showDialog("吃饭中...", mActivity!!.supportFragmentManager)
            ContextManager.getInstance().handler?.postDelayed({ProgressDialogUtil.dismissDialog(mActivity!!.supportFragmentManager)}, 2000)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_IMG) {
                Timber.d("file path: %s", data?.getStringExtra("data"))
            } else if (requestCode == REQUEST_CODE_SCAN) {
                Timber.d("qr msg: %s", data?.getStringExtra("qr_msg"))
            }
        }
    }

    private fun buildImgList(): ArrayList<String> {
        val imgList = ArrayList<String>()
        imgList.add("https://t7.baidu.com/it/u=2168645659,3174029352&fm=193&f=GIF")
        imgList.add("https://t7.baidu.com/it/u=2531125946,3055766435&fm=193&f=GIF")
        imgList.add("https://t7.baidu.com/it/u=1330338603,908538247&fm=193&f=GIF")
        imgList.add("https://t7.baidu.com/it/u=657578767,2750473856&fm=193&f=GIF")
        return imgList
    }

    private fun requestPermission() {
        acValid {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val permissionArray = arrayListOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    permissionArray.add(Manifest.permission.READ_PHONE_STATE)
                }
                PermissionAc.Companion.startActivityForResult(
                    mActivity!!,
                    REQUEST_CODE_PERMISSION,
                    permissionArray.toTypedArray())
            }
        }
    }
}
